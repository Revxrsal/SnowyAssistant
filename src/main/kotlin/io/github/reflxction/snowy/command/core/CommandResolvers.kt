/*
 * * Copyright 2019-2020 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing requiredPermissions and
 * limitations under the License.
 */
package io.github.reflxction.snowy.command.core

import com.google.common.collect.ImmutableMap
import io.github.reflxction.snowy.util.PluginIdentifier
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import java.awt.Color
import java.util.*

/**
 * A class which contains all custom resolvers
 */
class CommandResolvers {

    /**
     * A map of all resolvers
     */
    private val resolvers: MutableMap<Class<*>, Resolver<*>> = HashMap()

    /**
     * Registers the specified resolver. Although serves no real purpose, this helps
     * ensure passing appropriate types in the compiler.
     *
     * @param resolverClass Resolver class
     * @param resolver      The resolver adapter
     * @param <R>           The object to resolve
     */
    private fun <R> register(resolverClass: Class<R>, resolver: Resolver<R>) {
        resolvers[resolverClass] = resolver
    }

    /**
     * Resolves the specified type
     *
     * @param context  Context to resolve from
     * @param clazz    The class to resolve type of
     * @param argument Argument to resolve
     * @param <R>      Object type to resolve
     * @return The resolved object, or throws a [CommandCallback.CommandException].
     */
    @Suppress("UNCHECKED_CAST")
    fun <R> resolve(context: CommandContext, clazz: Class<R>, argument: String): R {
        return resolvers[clazz]?.resolve(context, argument) as R
    }

    /**
     * A resolver wrapper
     *
     * @param <R> Type to resolve
     */
    class Resolver<R>(private val resolver: ParameterResolver<R>, private val name: String) {

        /**
         * Resolves the specified argument to the type
         */
        fun resolve(context: CommandContext, arguement: String): R {
            return try {
                resolver(context, arguement)
            } catch (e: Exception) {
                //e.printStackTrace()
                throw CommandCallback.CommandException(String.format("**Invalid %s**: %s", name, arguement))
            }
        }
    }

    companion object {

        val PLUGINS: ImmutableMap<String, PluginIdentifier> = ImmutableMap.of(
                "spleefx", PluginIdentifier("SpleefX", Color(85, 191, 255)),
                "zombieapocalypse", PluginIdentifier("Zombie Apocalypse", Color(72, 187, 19))
        )

        private fun stripMention(original: String): String {
            return original
                    .replaceFirst("<".toRegex(), "")
                    .replaceFirst(">".toRegex(), "")
                    .replaceFirst("&".toRegex(), "")
                    .replaceFirst("@".toRegex(), "")
                    .replaceFirst("!".toRegex(), "")
        }
    }

    /**
     * Registers all pre-defined resolvers with their types
     */
    init {

        // register primitives
        register(Double::class.javaPrimitiveType!!, Resolver({ _, s -> s.toDouble() }, "number"))
        register(Int::class.javaPrimitiveType!!, Resolver({ _, s -> s.toInt() }, "number"))
        register(Float::class.javaPrimitiveType!!, Resolver({ _, s -> s.toFloat() }, "number"))

        // register wrappers
        register(String::class.java, Resolver({ _, s -> s }, "input"))
        register(Double::class.java, Resolver({ _, s -> s.toDouble() }, "number"))
        register(Int::class.java, Resolver({ _, s -> s.toInt() }, "number"))
        register(Float::class.java, Resolver({ _, s -> s.toFloat() }, "number"))

        // register JDA types
        register(User::class.java, Resolver({ c, s -> c.event.jda.getUserById(stripMention(s))!! }, "user"))
        register(Member::class.java, Resolver({ c, s ->
            try {
                return@Resolver c.event.guild.getMembersByEffectiveName(s, true)[0]
            } catch (e: Exception) {
                return@Resolver c.event.guild.getMemberById(stripMention(s))!!
            }
        }, "member"))
        register(Role::class.java, Resolver({ c, s ->
            return@Resolver c.event.guild.getRolesByName(s, true)[0] ?: c.event.guild.getRoleById(stripMention(s))!!
        }, "role"))

        // register custom types
        register(PluginIdentifier::class.java, Resolver({ _, s -> PLUGINS[s.toLowerCase()]!! }, "plugin"))
    }
}

/**
 * A resolver for parameter converting between strings and object types
 */
internal typealias ParameterResolver<R> = (CommandContext, String) -> R
