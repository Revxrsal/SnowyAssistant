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

import com.google.common.base.Preconditions
import io.github.reflxction.snowy.command.core.CommandCallback.CommandException
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.util.*
import java.util.function.Consumer

class CommandContext(val argsString: String, val event: GuildMessageReceivedEvent, private val wrapper: CommandWrapper, private val resolvers: CommandResolvers) {

    /**
     * The arguments as a list
     */
    val args: List<String> = if (argsString.isEmpty()) Collections.emptyList() else argsString.split(" ")

    /**
     * Replies to the channel wit the specified message
     *
     * @param message Message to reply with
     */
    fun reply(message: String) {
        event.channel.sendMessage(message).queue()
    }

    /**
     * Replies to the channel wit the specified message
     *
     * @param message Message to reply with
     */
    fun reply(message: Message) {
        event.channel.sendMessage(message).queue()
    }

    /**
     * Replies to the channel with the specified message, and runs the specified task after it is sent.
     */
    fun reply(message: String, onFinish: Consumer<Message>) {
        event.channel.sendMessage(message).queue(onFinish)
    }

    /**
     * Sends the specified embed builder
     *
     * @param embed Embed builder to send
     */
    fun reply(embed: EmbedBuilder) {
        event.channel.sendMessage(embed.build()).queue()
    }

    /**
     * Stops executing the command and throws an invalid usage exception
     */
    fun invalidUsage() {
        throw CommandException("Invalid usage. Try **!".plus(wrapper.name).plus(" ").plus(wrapper.parameters).plus("**"))
    }

    /**
     * Stops executing the command and throws a no permission exception
     */
    fun noPermission() {
        throw CommandException("**You do not have permission to use this command!**")
    }

    /**
     * Resolves the specified index with the type
     *
     * @param R     Object type
     * @param index Index to resolve
     * @param type  Type to resolve for
     * @return The resolved object
     */
    fun <R> resolve(index: Int, type: Class<R>): R {
        return resolve(args[index], type)
    }

    /**
     * Resolves the specified index with the type
     *
     * @param R     Object type
     * @param text  Text to resolve from
     * @param type  Type to resolve for
     * @return The resolved object
     */
    fun <R> resolve(text: String, type: Class<R>): R {
        return resolvers.resolve(this, type, text)
    }

    /**
     * Joins the arguments from the specified index
     *
     * @param index Index to begin from
     */
    fun join(index: Int, endIndex: Int = args.size): String {
        Preconditions.checkArgument(endIndex <= args.size, "End index can't be greater than the arguments size!")
        val builder: StringBuilder = java.lang.StringBuilder()
        for (i in index until endIndex) {
            builder.append(args[i]).append(" ")
        }
        return builder.toString().trim()
    }

    /**
     * Returns whether does the command contain any inputted arguments
     *
     * @return Whether does the command have any additional arguments
     */
    fun hasArgs(): Boolean {
        return args.isNotEmpty()
    }

}
