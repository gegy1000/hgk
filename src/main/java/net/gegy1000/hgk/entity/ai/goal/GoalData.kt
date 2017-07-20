package net.gegy1000.hgk.entity.ai.goal

class GoalData(init: (GoalData) -> Unit = {}) {
    private val data = HashMap<String, Any>()

    init {
        init(this)
    }

    operator fun <T: Any> set(name: String, value: T): GoalData {
        data.put(name, value)
        return this
    }

    operator fun <T: Any> get(name: String): T {
        if (data.containsKey(name)) {
            try {
                return data[name] as T
            } catch (e: TypeCastException) {
            }
        }
        throw IllegalStateException("Found invalid type for goal data $name")
    }
}
