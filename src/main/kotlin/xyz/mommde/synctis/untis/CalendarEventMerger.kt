package xyz.mommde.synctis.untis

import xyz.mommde.synctis.calendar.SynctisCalendarEvent

/**
 * Will merge two events into one. This means it will start when the first starts and end when the second ends.
 *
 * This can only happen when doEventsEqual returns true.
 */
fun mergeEvents(events: List<SynctisCalendarEvent>): List<SynctisCalendarEvent> {
    val startToEventMap = events.associateBy { it.start }.toMutableMap()
    events.forEach {
        val otherEvent = startToEventMap[it.end]
        if (otherEvent != null && doEventsEqual(it, otherEvent)) {
            return mergeEvents(events.toMutableList().apply {
                remove(it)
                remove(otherEvent)
                add(SynctisCalendarEvent(
                    id = it.id,
                    start = it.start,
                    end = otherEvent.end,
                    location = it.location,
                    teacher = it.teacher,
                    name = it.name,
                    color = it.color,
                    homework = it.homework
                ))
            })
        }
    }
    return events
}

private fun doEventsEqual(first: SynctisCalendarEvent, second: SynctisCalendarEvent): Boolean {
    return first.name == second.name &&
            first.location == second.location &&
            first.teacher == second.teacher
}