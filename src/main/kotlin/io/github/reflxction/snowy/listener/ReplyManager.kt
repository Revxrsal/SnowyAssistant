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
package io.github.reflxction.snowy.listener

import io.github.reflxction.snowy.SnowyBot
import io.github.reflxction.snowy.command.ticket.CloseCommand
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.concurrent.ScheduledFuture

object ReplyManager : ListenerAdapter() {

    /**
     * A list of all automated replies
     */
    private val replies: MutableMap<String, Reply> = HashMap()

    /**
     * Registers the specified reply
     *
     * @param text Text to check for
     * @param reply Reply to reply with
     */
    private fun register(text: String, reply: Reply) {
        replies[text] = reply
    }

    /**
     * Listens for all replies in messages
     *
     * @param event Guild message event data
     */
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (event.message.author == event.jda.selfUser || event.message.contentRaw.contains("close") || event.message.contentRaw.contains("!reply")) return
        val message: String = event.message.contentRaw
        for (replyEntry in replies) {
            if (message.contains(replyEntry.key, true)) {
                replyEntry.value(event)
                break
            }
        }
        for (reply in SnowyBot.getDefaultReplies().replies) {
            if (message.contains(reply.requirement)) {
                reply.invoke(event)
                break
            }
        }
        val closedChannel: ScheduledFuture<*> = CloseCommand.BEING_DELETED.remove(event.channel.id) ?: return
        closedChannel.cancel(true)
        event.channel.sendMessage("Closing cancelled.").queue()
    }

    override fun onTextChannelDelete(event: TextChannelDeleteEvent) {
        SnowyBot.getTicketsProvider().closeTicket(event.channel)
    }

    /**
     * Registers all pre-defined replies
     */
    init {
        register("latepaypal") { event -> (event.channel.sendMessage(AutomatedReply.PAYPAL).queue()) }
        register("Could not load 'plugins/SpleefX-PAPI") { event -> event.channel.sendMessage(AutomatedReply.PAPI_PLUGIN.format(event.member?.effectiveName)).queue() }
    }

}