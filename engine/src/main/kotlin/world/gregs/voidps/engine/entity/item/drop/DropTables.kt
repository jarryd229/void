package world.gregs.voidps.engine.entity.item.drop

import org.koin.dsl.module
import world.gregs.voidps.engine.data.file.FileStorage
import world.gregs.voidps.engine.timedLoad
import world.gregs.voidps.engine.utility.getProperty
import world.gregs.voidps.engine.utility.toIntRange

val dropTableModule = module {
    single(createdAtStart = true) { DropTables().load() }
}

@Suppress("UNCHECKED_CAST")
class DropTables {

    private lateinit var tables: Map<String, DropTable>

    fun get(key: String) = tables[key]

    fun getValue(key: String) = tables.getValue(key)

    fun load(storage: FileStorage = world.gregs.voidps.engine.utility.get(), path: String = getProperty("dropsPath")): DropTables {
        timedLoad("drop table") {
            load(storage.load<Map<String, Any>>(path))
        }
        return this
    }

    fun load(data: Map<String, Any>): Int {
        tables = data.map { (key, value) -> key to loadTable(data, value as Map<String, Any>).build() }.toMap()
        return tables.size
    }

    private fun loadTable(names: Map<String, Any>, map: Map<String, Any>): DropTable.Builder {
        val table = DropTable.Builder()
        if (map.containsKey("chance")) {
            table.withChance(map.chance())
        }
        if (map.containsKey("roll")) {
            table.withRoll(map.roll())
        }
        if (map.containsKey("type")) {
            table.withType(TableType.byName(map["type"] as String))
        }
        if (map.containsKey("drops")) {
            val drops = map["drops"] as List<Map<String, Any>>
            for (drop in drops) {
                if (drop.containsKey("drops") || drop.containsKey("id") && names.containsKey(drop.id())) {
                    table.addDrop(loadTable(names, drop).build())
                } else if(drop.containsKey("id")) {
                    table.addDrop(ItemDrop(drop.id(), drop.amount(), drop.chance()))
                }
            }
        } else if (map.containsKey("id")) {
            val name = map.id()
            check(names.contains(name)) { "Unable to find drop table link with name '$name'" }
            table.addDrop(
                loadTable(names, names[name] as Map<String, Any>)
                    .withChance(map.chance())
                    .build()
            )
        }
        return table
    }

    private fun Map<String, Any>.id() = this["id"] as String
    private fun Map<String, Any>.amount(): IntRange = if (containsKey("amount")) {
        val amount = this["amount"]
        if (amount is String && amount.contains("-")) {
            amount.toIntRange(inclusive = true)
        } else {
            amount as Int..amount
        }
    } else {
        1..1
    }

    private fun Map<String, Any>.roll() = (this["roll"] as? Int) ?: 1
    private fun Map<String, Any>.chance() = (this["chance"] as? Int) ?: 1
}