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

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import io.github.reflxction.snowy.SnowyBot
import io.github.reflxction.snowy.command.core.CommandCallback.CommandException
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import org.slf4j.Logger

/**
 * Command processor for handling commands
 */
class CommandHandler(var jda: JDA) : EventListener {

    /**
     * A map of all registered commands
     */
    val commands: MutableMap<String, CommandWrapper> = HashMap()

    /**
     * A multimap which maps all commands by their categories.
     */
    private val byCategories: Multimap<CommandCategory, CommandWrapper> = ArrayListMultimap.create()

    companion object {
        @JvmField
        val EVENT_WAITER = EventWaiter()
    }

    /**
     * The resolvers
     */
    private val resolvers: CommandResolvers = CommandResolvers()

    /**
     * Registers this commands handler
     *
     * @param logger Logger of the bot
     * @return This handler instance
     */
    fun registerListeners(logger: Logger?): CommandHandler {
        logger?.info("Registering command handler")
        jda.addEventListener(this, EVENT_WAITER)
        return this
    }

    /**
     * Registers the specified commands
     *
     * @param commands Commands to register
     */
    fun register(vararg commands: CommandCallback): CommandHandler {
        for (command in commands) {
            if (!command.javaClass.isAnnotationPresent(BotCommand::class.java))
                throw IllegalStateException("Class is not annotated with BotCommand!")

            val ann: BotCommand = command.javaClass.getAnnotation(BotCommand::class.java)
            val wrapper = CommandWrapper(ann.name, ann.description, ann.parameters, ann.aliases.toList(), ann.requiredPermissions, ann.allowedRoles, ann.help, ann.category, ann.minimumArguments, command)
            this.commands[ann.name] = wrapper
            for (alias in ann.aliases)
                this.commands[alias] = wrapper
            if (ann.help)
                byCategories.put(ann.category, wrapper)
        }
        return this
    }

    /**
     * Handles any [GenericEvent][net.dv8tion.jda.api.events.GenericEvent].
     *
     *
     * To get specific events with Methods like `onMessageReceived(MessageReceivedEvent event)`
     * take a look at: [ListenerAdapter][net.dv8tion.jda.api.hooks.ListenerAdapter]
     *
     * @param e The Event to handle.
     */
    override fun onEvent(e: GenericEvent) {
        if (e !is GuildMessageReceivedEvent) return
        if (!e.message.contentRaw.startsWith(SnowyBot.PREFIX)) return
        val member = e.member ?: return
        val event: GuildMessageReceivedEvent = e
        val commandText = event.message.contentRaw.substring(1).split(" ".toRegex(), 2).toTypedArray()
        val wrapper = commands[commandText[0]] ?: return
        val context = CommandContext(if (commandText.size == 1) "" else commandText[1], event, wrapper, resolvers)
        try {
            for (permission in wrapper.permissions)
                if (!member.hasPermission(permission)) context.noPermission()
            if (wrapper.roles.isNotEmpty() && wrapper.roles.asList().stream().map(event.guild::getRoleById).noneMatch(member.roles::contains))
                context.noPermission()
            if (context.args.size < wrapper.minimumArguments)
                context.invalidUsage()
            wrapper.command.onProcess(context)
        } catch (e: CommandException) {
            event.channel.sendMessage(e.message.toString()).queue()
        }
    }
}