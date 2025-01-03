/* Copyright (C) 2023-2025 Eroica
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

// dllmain.cpp : Defines the entry point for the DLL application.
#include "pch.h"

#include <string>

#include <dwmapi.h>
#include <jni.h>
#include <Windows.h>
#include <windowsx.h>

#define WINDOWS_11_22H2 22621

/* DWM constants */
int DWM_trueValue = 0x01;
int DWM_falseValue = 0x00;
int DWMBACKDROP_disable = 0x01;
int DWMBACKDROP_mica = 0x02;
int DWMBACKDROP_transient = 0x03;
int DWMBACKDROP_tabbed = 0x04;

/* Size constants in logical points, must be scaled by display scaling */
const int WINDOW_CONTROL_PADDING = 144;
const int HEADER_BAR_HEIGHT = 48;

int drag_area_x = 0;
int drag_area_width = 0;
double display_scale = 1.0;

WNDPROC wpOrigWndProc;

/**
 * Adapted from:
 * dgellow, 2022-01-18
 * https://stackoverflow.com/a/70753913
 * CC BY-SA 3.0
 * https://creativecommons.org/licenses/by-sa/3.0/
 */
bool is_dark_theme() {
	std::string value;
	DWORD BufferSize = sizeof(value);

	auto result = RegGetValue(
		HKEY_LOCAL_MACHINE,
		"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
		"AppsUseLightTheme",
		RRF_RT_REG_SZ,
		nullptr,
		&value,
		&BufferSize
	);

	if (result == ERROR_SUCCESS) {
		return value.data() == "0";
	} else {
		/* Something went wrong, just assume dark mode is not set. */
		return false;
	}
}

int GetBuildNumber() {
	std::string value;
	DWORD BufferSize = sizeof(value);

	auto result = RegGetValue(
		HKEY_LOCAL_MACHINE,
		"SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion",
		"CurrentBuild",
		RRF_RT_REG_SZ,
		0,
		&value,
		&BufferSize
	);

	if (result == ERROR_SUCCESS) {
		return std::atoi(value.data());
	} else {
		return 0;
	}
}

LRESULT hitTests[3][3] = {
	{ HTTOPLEFT,    HTTOP,    HTTOPRIGHT },
	{ HTLEFT,       HTNOWHERE,     HTRIGHT },
	{ HTBOTTOMLEFT, HTBOTTOM, HTBOTTOMRIGHT },
};

LRESULT HitTestNCA(HWND hWnd, WPARAM wParam, LPARAM lParam) {
	POINT ptMouse = { GET_X_LPARAM(lParam), GET_Y_LPARAM(lParam) };
	RECT rcWindow, rcFrame;
	GetWindowRect(hWnd, &rcWindow);
	AdjustWindowRectEx(&rcFrame, WS_OVERLAPPEDWINDOW & ~WS_CAPTION, FALSE, NULL);

	USHORT uRow = 1;
	USHORT uCol = 1;

	if (ptMouse.y >= rcWindow.top && ptMouse.y < rcWindow.top + 4) {
		uRow = 0;
	} else if (ptMouse.y < rcWindow.bottom && ptMouse.y >= rcWindow.bottom - 4) {
		uRow = 2;
	}

	if (ptMouse.x >= rcWindow.left && ptMouse.x < rcWindow.left + 4) {
		uCol = 0;
	} else if (ptMouse.x < rcWindow.right && ptMouse.x >= rcWindow.right - 4) {
		uCol = 2;
	}

	if ((ptMouse.y >= rcWindow.top + 4 && ptMouse.y < rcWindow.top + HEADER_BAR_HEIGHT * display_scale && ptMouse.x < rcWindow.right - 8 && ptMouse.x >= rcWindow.right - WINDOW_CONTROL_PADDING * display_scale)
		|| (ptMouse.y >= rcWindow.top + 4 && ptMouse.y < rcWindow.top + HEADER_BAR_HEIGHT * display_scale && ptMouse.x > rcWindow.left + drag_area_x && ptMouse.x < rcWindow.left + drag_area_x + drag_area_width)) {
		return HTCAPTION;
	} else {
		return hitTests[uRow][uCol];
	}
}

LRESULT HeaderBarProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam, bool* pfCallDWP) {
	LRESULT lRet = 0;
	HRESULT hr = S_OK;
	bool fCallDWP = true;

	fCallDWP = !DwmDefWindowProc(hWnd, message, wParam, lParam, &lRet);

	if ((message == WM_NCCALCSIZE) && (wParam == TRUE)) {
		NCCALCSIZE_PARAMS* pncsp = reinterpret_cast<NCCALCSIZE_PARAMS*>(lParam);

		pncsp->rgrc[0].left = pncsp->rgrc[0].left + 8;
		pncsp->rgrc[0].top = pncsp->rgrc[0].top + 0;
		pncsp->rgrc[0].right = pncsp->rgrc[0].right - 8;
		pncsp->rgrc[0].bottom = pncsp->rgrc[0].bottom - 0;

		lRet = 0;
		fCallDWP = false;
	}

	if ((message == WM_NCHITTEST) && (lRet == 0)) {
		lRet = HitTestNCA(hWnd, wParam, lParam);

		if (lRet != HTNOWHERE) {
			fCallDWP = false;
		}
	}

	*pfCallDWP = fCallDWP;

	return lRet;
}

LRESULT MainWndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam) {
	switch (message) {
	case WM_DESTROY:
		SetWindowLongPtr(hWnd, GWLP_WNDPROC, (LONG_PTR)wpOrigWndProc);
		return LRESULT(0);
	default:
		return CallWindowProc(wpOrigWndProc, hWnd, message, wParam, lParam);
	}
}

LRESULT CALLBACK AppWndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam) {
	bool fCallDWP = true;
	BOOL fDwmEnabled = FALSE;
	LRESULT lRet = 0;
	HRESULT hr = S_OK;

	hr = DwmIsCompositionEnabled(&fDwmEnabled);
	if (SUCCEEDED(hr)) {
		lRet = HeaderBarProc(hWnd, message, wParam, lParam, &fCallDWP);
	}

	if (fCallDWP) {
		lRet = MainWndProc(hWnd, message, wParam, lParam);
	}

	return lRet;
}

void set_mica(HWND hWnd, BOOL is_set_mica) {
	if (is_set_mica) {
		if (GetBuildNumber() >= WINDOWS_11_22H2) {
			DwmSetWindowAttribute(hWnd, 38, &DWMBACKDROP_mica, sizeof(int));
		} else {
			DwmSetWindowAttribute(hWnd, 1029, &DWM_trueValue, sizeof(int));
		}
	} else {
		DwmSetWindowAttribute(hWnd, 38, &DWMBACKDROP_disable, sizeof(int));
	}
}

void set_headerbar(HWND hWnd, BOOL is_set_headerbar) {
	if (is_set_headerbar) {
		wpOrigWndProc = (WNDPROC)SetWindowLongPtr(hWnd, GWLP_WNDPROC, (LONG_PTR)AppWndProc);
		SetWindowPos(hWnd, NULL, 0, 0, 0, 0, SWP_FRAMECHANGED | SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_NOOWNERZORDER);
	} else {
		SetWindowLongPtr(hWnd, GWLP_WNDPROC, (LONG_PTR)wpOrigWndProc);
		SetWindowPos(hWnd, NULL, 0, 0, 0, 0, SWP_FRAMECHANGED | SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_NOOWNERZORDER);
	}
}

extern "C" JNIEXPORT int JNICALL
Java_earth_groundctrl_fluent_lib_Windows_buildnumber(JNIEnv* env, jobject) {
	return GetBuildNumber();
}

extern "C" JNIEXPORT int JNICALL
Java_earth_groundctrl_fluent_lib_Windows_setmica(JNIEnv * env, jobject, jstring title, jboolean use_mica) {
	const char* windowTitle = env->GetStringUTFChars(title, NULL);
	HWND hWnd = FindWindow(NULL, windowTitle);

	if (hWnd != NULL) {
		set_mica(hWnd, static_cast<BOOL>(use_mica));
	} else {
		env->ReleaseStringUTFChars(title, windowTitle);
		return -1;
	}

	env->ReleaseStringUTFChars(title, windowTitle);

	return 0;
}

extern "C" JNIEXPORT int JNICALL
Java_earth_groundctrl_fluent_lib_Windows_setheaderbar(JNIEnv * env, jobject, jstring title, jboolean use_headerbar) {
	const char* windowTitle = env->GetStringUTFChars(title, NULL);
	HWND hWnd = FindWindow(NULL, windowTitle);

	if (hWnd != NULL) {
		set_headerbar(hWnd, static_cast<BOOL>(use_headerbar));
	} else {
		env->ReleaseStringUTFChars(title, windowTitle);
		return -1;
	}

	env->ReleaseStringUTFChars(title, windowTitle);

	return 0;
}

extern "C" JNIEXPORT bool JNICALL
Java_earth_groundctrl_fluent_lib_Windows_isdarkmode(JNIEnv * env, jobject) {
	return is_dark_theme();
}

extern "C" JNIEXPORT void JNICALL
Java_earth_groundctrl_fluent_lib_Windows_setdragarea(
	JNIEnv * env, jobject,
	jint x, jint width, jdouble scale
) {
	drag_area_x = (int)(x * scale);
	drag_area_width = (int)(width * scale);
	display_scale = (double)scale;
}

extern "C" JNIEXPORT int JNICALL
Java_earth_groundctrl_fluent_lib_Windows_subclass(
	JNIEnv * env,
	jobject,
	jstring title,
	jboolean useMica,
	jboolean useHeaderBar
) {
	BOOL use_mica = static_cast<BOOL>(useMica);
	BOOL use_headerbar = static_cast<BOOL>(useHeaderBar);
	const char* windowTitle = env->GetStringUTFChars(title, NULL);
	HWND hWnd = FindWindow(NULL, windowTitle);

	if (hWnd != NULL) {
		set_mica(hWnd, use_mica);
		set_headerbar(hWnd, use_headerbar);
	} else {
		env->ReleaseStringUTFChars(title, windowTitle);
		return -1;
	}

	env->ReleaseStringUTFChars(title, windowTitle);

	return 0;
}
