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
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import java.util.concurrent.TimeUnit

@BotCommand(
        name = "conversation",
        description = "Start a private conversation with someone",
        category = CommandCategory.TICKETS,
        parameters = "create <participant> | close | add <participant> | topic <topic>",
        requiredPermissions = [Permission.ADMINISTRATOR],
        minimumArguments = 1,
        aliases = ["convo", "private"]
)
object ConversationCommand : CommandCallback {

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        if (context.args.size == 1) {
            if ("close" == context.args[0]) {
                closePrivateChat(context)
                return
            }
            val member: Member = context.resolve(context.join(0), Member::class.java)
            val chat: PrivateChat = SnowyBot.getConversationsProvider().openChat(context.event.author, member.user, context.event.jda)
            context.reply("Successfully created chat with **${member.effectiveName}**. Head to <#${chat.channel}> and enjoy the privacy!")
            return
        } else {
            when (context.args[0]) {
                "create", "start" -> {
                    val member: Member = context.resolve(context.join(1), Member::class.java)
                    val chat: PrivateChat = SnowyBot.getConversationsProvider().openChat(context.event.author, member.user, context.event.jda)
                    context.reply("Successfully created chat with **${member.effectiveName}**. Head to <#${chat.channel}> and enjoy the privacy!")
                    return
                }
                "add", "addmember" -> {
                    val target = context.resolve(context.join(1), Member::class.java)
                    val member = context.event.member ?: return
                    val convo = SnowyBot.getConversationsProvider().getChat(context.event.message)
                            ?: throw CommandCallback.CommandException("This channel is not a conversation!")
                    if (!convo.canEdit(member)) context.noPermission()
                    val channel = context.event.guild.getTextChannelById(convo.channel) ?: return
                    channel.upsertPermissionOverride(target).setAllow(Permission.MESSAGE_READ).queue()
                    context.reply("Successfully added **${target.effectiveName}** to the conversation.")
                    return
                }
                "title", "desc", "topic", "description" -> {
                    val desc = context.join(1)
                    val convo = SnowyBot.getConversationsProvider().getChat(context.event.message)
                            ?: throw CommandCallback.CommandException("This channel is not a conversation!")
                    if (desc.length <= 1024) {
                        context.event.guild.getTextChannelById(convo.channel)?.manager?.setTopic(desc)?.queue()
                        context.reply("Successfully updated conversation topic to **$desc**.")
                        return
                    } else throw CommandCallback.CommandException("The topic length must be less than or equal to 1024 characters!")
                }
            }
        }
        context.invalidUsage()
    }

    /**
     * Handles the close command when the user is closing a conversation
     */
    private fun closePrivateChat(context: CommandContext) {
        val chat: PrivateChat = SnowyBot.getConversationsProvider().getChat(context.event.message)
                ?: throw CommandCallback.CommandException("This channel is not a conversation!")
        val member: Member = context.event.member ?: return
        if (chat.canEdit(member)) {
            val future = CloseCommand.BEING_DELETED.getOrDefault(chat.channel, null)
            if (future == null) {
                context.event.message.textChannel.sendMessage("**Closing conversation in 15 seconds!**\n\nSend any message to cancel.").queue()
                CloseCommand.BEING_DELETED[context.event.channel.id] = context.event.message.textChannel.delete().queueAfter(15, TimeUnit.SECONDS)
            } else
                throw CommandCallback.CommandException("This chat is already being closed!")
        } else context.noPermission()
    }

}