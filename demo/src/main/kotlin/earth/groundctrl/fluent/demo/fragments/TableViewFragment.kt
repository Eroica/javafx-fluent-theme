package earth.groundctrl.fluent.demo.fragments

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

class Weapon {
    private val id = SimpleStringProperty("")

    fun getId() = id.get()
    fun setId(value: String) {
        id.set(value)
    }

    private val name = SimpleStringProperty("")

    fun getName() = name.get()
    fun setName(value: String) {
        name.set(value)
    }

    private val description = SimpleStringProperty("")

    fun getDescription() = description.get()
    fun setDescription(value: String) {
        description.set(value)
    }

    private val quantity = SimpleIntegerProperty(0)

    fun getQuantity() = quantity.get()
    fun setQuantity(value: Int) {
        quantity.set(value)
    }
}
