package xyz.mommde.synctis.untis

import xyz.mommde.synctis.calendar.SynctisCalendarEvent

/**
 * Will merge two events into one. This means it will start when the first starts and end when the second ends.
 *
 * This can only happen when doEventsEqual returns true.
 */
fun mergeEvents(events: List<SynctisCalendarEvent>): List<SynctisCalendarEvent> {
    val result = mutableListOf<SynctisCalendarEvent>()
    events.groupBy { it.start.date }.forEach { groupStartingSameDate ->
        val startTimeToEvent = groupStartingSameDate.value.map {
            it.start.time to it
        }.toMap().toMutableMap()
        groupStartingSameDate.value.forEach { event ->
            val eventThatStartsWhenCurrentEnds = startTimeToEvent[event.start.time] ?: return@forEach
            if (doEventsEqual(event, eventThatStartsWhenCurrentEnds)) {
                result.add(event.copy(
                    homework = event.homework + "\n" + eventThatStartsWhenCurrentEnds.homework,
                    start = event.start,
                    end = eventThatStartsWhenCurrentEnds.end,
                ))
                startTimeToEvent.remove(eventThatStartsWhenCurrentEnds.start.time)
            } else {
               result.add(event)
            }
        }
    }
    if (result.size != events.size) {
        return mergeEvents(result)
    }
    return result
}

private fun doEventsEqual(first: SynctisCalendarEvent, second: SynctisCalendarEvent): Boolean {
    return first.name == second.name &&
            first.color == second.color &&
            first.location == second.location &&
            first.teacher == second.teacher
}