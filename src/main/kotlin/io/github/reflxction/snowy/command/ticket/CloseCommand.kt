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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.reflxction.snowy.command.ticket

import io.github.reflxction.snowy.SnowyBot
import io.github.reflxction.snowy.command.core.BotCommand
import io.github.reflxction.snowy.command.core.CommandCallback
import io.github.reflxction.snowy.command.core.CommandCategory
import io.github.reflxction.snowy.command.core.CommandContext
import io.github.reflxction.snowy.conversation.PrivateChat
import io.github.reflxction.snowy.ticket.Ticket
import net.dv8tion.jda.api.entities.Member
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

@BotCommand(
        name = "close",
        description = "Close the ticket/private chat in the current channel",
        category = CommandCategory.TICKETS,
        minimumArguments = 0
)
object CloseCommand : CommandCallback {

    @JvmField
    val BEING_DELETED: MutableMap<String, ScheduledFuture<*>> = HashMap()

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        val chatType: Any = SnowyBot.getTicketsProvider().getTicket(context.event.message)
                ?: SnowyBot.getConversationsProvider().getChat(context.event.message)
                ?: throw CommandCallback.CommandException("**This channel is not a ticket or conversation!**")
        val member: Member = context.event.member ?: return
        if (chatType is Ticket) {
            if (chatType.canEdit(member)) {
                val future = BEING_DELETED.getOrDefault(chatType.channel, null)
                if (future == null) {
                    context.event.message.textChannel.sendMessage("**Closing ticket in 15 seconds!**\n\nSend any message to cancel.").queue()
                    BEING_DELETED[context.event.channel.id] = context.event.message.textChannel.delete().queueAfter(15, TimeUnit.SECONDS)
                } else
                    throw CommandCallback.CommandException("This channel is already being closed!")
            } else context.noPermission()
        } else if (chatType is PrivateChat) {
            closePrivateChat(context, chatType)
        }
    }

    /**
     * Handles the close command when the user is closing a conversation
     */
    private fun closePrivateChat(context: CommandContext, chat: PrivateChat) {
        val member: Member = context.event.member ?: return
        if (chat.canEdit(member)) {
            val future: ScheduledFuture<*>? = BEING_DELETED.getOrDefault(chat.channel, null)
            if (future == null) {
                context.event.message.textChannel.sendMessage("**Closing conversation in 15 seconds!**\n\nSend any message to cancel.").queue()
                BEING_DELETED[context.event.channel.id] = context.event.message.textChannel.delete().queueAfter(15, TimeUnit.SECONDS)
            } else
                throw CommandCallback.CommandException("This chat is already being closed!")
        } else context.noPermission()
    }
}