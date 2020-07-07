package me.arasple.mc.trmenu.utils

import io.izzel.taboolib.internal.gson.JsonParser
import me.arasple.mc.trmenu.modules.configuration.property.Property
import org.apache.commons.lang.math.NumberUtils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration


/**
 * @author Arasple
 * @date 2020/5/30 12:26
 */
object Utils {

    fun asList(any: Any?): List<String> {
        if (any == null) return mutableListOf()
        val result = mutableListOf<String>()
        when (any) {
            is List<*> -> any.forEach { result.add(it.toString()) }
            is MemorySection -> any.getValues(false).forEach { result.add("${it.key}:${it.value}") }
            else -> result.add(any.toString())
        }
        return result
    }

    fun asArray(any: Any?): Array<String> = asList(any).toTypedArray()

    fun asBoolean(any: Any?): Boolean = any.toString().toBoolean()

    fun asInt(any: Any?, def: Int): Int = NumberUtils.toInt(any.toString(), def)

    fun asLong(any: Any?, def: Long): Long = NumberUtils.toLong(any.toString(), def)

    @Suppress("UNCHECKED_CAST")
    fun <T> asLists(any: Any): List<List<T>> {
        val results = mutableListOf<List<T>>()
        when (any) {
            is List<*> -> {
                if (any.isNotEmpty()) {
                    if (any[0] is List<*>) any.forEach { results.add(it as List<T>) }
                    else results.add(any as List<T>)
                }
            }
            else -> results.add(listOf(any as T))
        }
        return results
    }

    @Suppress("DEPRECATION")
    fun isJson(string: String): Boolean = try {
        JsonParser().parse(string); true
    } catch (e: Throwable) {
        false
    }

    fun asSection(any: Any): MemorySection? = YamlConfiguration().let {
        when (any) {
            is MemorySection -> return any
            is List<*> -> any.forEach { any ->
                val args = any.toString().split(Regex(":"), 2)
                if (args.size == 2) it.set(args[0], args[1])
            }
        }
        return@let null
    }

    fun getSectionKey(section: ConfigurationSection?, property: Property): String {
        return section?.getKeys(false)?.firstOrNull { it.matches(property.regex) } ?: property.default
    }

    private fun getSectionKey(section: ConfigurationSection?, regex: Regex, default: String): String {
        return section?.getKeys(false)?.firstOrNull { it.matches(regex) } ?: default
    }

}