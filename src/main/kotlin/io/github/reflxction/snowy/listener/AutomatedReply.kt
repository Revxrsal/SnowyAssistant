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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

/**
 * A class which contain constants of all automated replies
 */
object AutomatedReply {

    /**
     * Sent when "PayPal" is mentioned
     */
    const val PAYPAL = "**Due to PayPal's policies in the developer's country**, they have to approve payments manually. \n" +
            "\n" +
            "**Meaning**: After purchasing the plugin, you may need to wait until the developer approves the payment and gives you access to the resource manually. This should usually take **1-8 hours** maximum.\n" +
            "\n" +
            "**Timing where acquiring access should be fast**: 7 AM EST until 3 PM EST\n" +
            "**Timing where acquiring access could take some time**: 4 PM EST until 11 PM EST\n" +
            "\n" +
            "Sorry for the delay!"

    const val PAPI_PLUGIN = "**%s**, SpleefX-PAPI is not a plugin! Place it in `/PlaceholderAPI/expansions`, or download it using the `/papi ecloud` command."

}

typealias Reply = (event: GuildMessageReceivedEvent) -> Unit

/**
 * Represents a standard reply
 */
class StandardReply(val requirement: String, val message: String) : Reply {
    override fun invoke(event: GuildMessageReceivedEvent) {
        event.channel.sendMessage(message).queue()
    }
}

class DefaultReplyData {
    val replies: MutableList<StandardReply> = ArrayList()
}