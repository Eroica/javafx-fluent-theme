package earth.groundctrl.fluent.ui

import javafx.animation.Interpolator

/* Common_themeresources_any.xaml */
/* <x:String x:Key="ControlFastOutSlowInKeySpline">0,0,0,1</x:String> */
val FastOutSlowInSpline: Interpolator = Interpolator.ofSpline(0.0, 0.0, 0.0, 1.0)
val FluentInterpolator = FastOutSlowInSpline

/* <x:String x:Key="ControlNormalAnimationDuration">00:00:00.250</x:String> */
val NormalAnimationDuration = 250.0

/* <x:String x:Key="ControlFastAnimationDuration">00:00:00.167</x:String> */
val FastAnimationDuration = 167.0

/* <x:String x:Key="ControlFastAnimationAfterDuration">00:00:00.168</x:String> */
val FastAnimationAfterDuration = 168.0

/* <x:String x:Key="ControlFasterAnimationDuration">00:00:00.083</x:String> */
val FasterAnimationDuration = 83.0
