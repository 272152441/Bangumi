package com.zxwork.bmg.data.mapper

import com.zxwork.bmg.data.model.CalendarModel
import com.zxwork.bmg.data.model.CharacterModel
import com.zxwork.bmg.data.model.EpisodeModel
import com.zxwork.bmg.data.model.SubjectDetailModel
import com.zxwork.bmg.data.model.SubjectModel
import com.zxwork.bmg.domain.model.Calendar
import com.zxwork.bmg.domain.model.Character
import com.zxwork.bmg.domain.model.Episode
import com.zxwork.bmg.domain.model.Subject
import com.zxwork.bmg.domain.model.SubjectDetail
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

fun CalendarModel.toDomain() = Calendar(
    weekday = weekday.toDomain(),
    items = items.map { it.toDomain() },
)

fun CalendarModel.Weekday.toDomain() = Calendar.Weekday(
    id = id,
    cn = cn,
    en = en,
    ja = ja
)

fun CalendarModel.Item.toDomain() = Calendar.Item(
    id = id,
    name = name,
    nameCn = nameCn,
    airDate = airDate,
    airWeekday = airWeekday,
    images = images?.toDomain(),
    score = rating?.score,
    rank = rank,
    summary = summary
)

fun CalendarModel.Item.Images.toDomain() = Calendar.Images(
    common = common ?: "",
    medium = medium ?: "",
    small = small ?: "",
    grid = grid ?: "",
    large = large ?: ""
)

fun SubjectModel.toDomain() = Subject(
    id = id ?: 0,
    type = type ?: 0,
    name = name ?: "",
    nameCn = nameCn ?: "",
    summary = summary ?: "",
    date = date ?: "",
    platform = platform ?: "",
    relation = relation ?: "",
    images = images?.toDomain()
)

fun SubjectModel.Images.toDomain() = Subject.Images(
    large = large ?: "",
    common = common ?: "",
    medium = medium ?: "",
    small = small ?: "",
    grid = grid ?: ""
)

fun SubjectDetailModel.toDomain() = SubjectDetail(
    id = id,
    type = type,
    name = name,
    nameCn = nameCn,
    summary = summary,
    date = date ?: "",
    platform = platform ?: "",
    images = images?.toDomain(),
    rating = rating?.toDomain(),
    collection = collection?.toDomain(),
    tags = tags?.map { it.toDomain() } ?: emptyList(),
    infobox = infobox?.map { it.toDomain() } ?: emptyList()
)

fun SubjectDetailModel.Images.toDomain() = Subject.Images(
    large = large ?: "",
    common = common ?: "",
    medium = medium ?: "",
    small = small ?: "",
    grid = grid ?: ""
)

fun SubjectDetailModel.Rating.toDomain() = SubjectDetail.Rating(
    score = score,
    total = total,
    rank = rank
)

fun SubjectDetailModel.Collection.toDomain() = SubjectDetail.Collection(
    wish = wish,
    collect = collect,
    doing = doing,
    onHold = onHold,
    dropped = dropped
)

fun SubjectDetailModel.Tag.toDomain() = SubjectDetail.Tag(
    name = name,
    count = count
)

fun SubjectDetailModel.InfoBoxItem.toDomain() = SubjectDetail.InfoBoxItem(
    key = key,
    value = when (val v = value) {
        is JsonPrimitive -> v.contentOrNull ?: ""
        is JsonArray -> v.joinToString(", ") {
            (it as? JsonPrimitive)?.contentOrNull ?: it.toString()
        }

        else -> v.toString()
    }
)

fun EpisodeModel.toDomain() = Episode(
    id = id,
    sort = sort,
    type = type,
    name = name,
    nameCn = nameCn,
    airdate = airdate ?: "",
    duration = duration ?: "",
    desc = desc ?: ""
)

fun CharacterModel.toDomain() = Character(
    id = id,
    name = name,
    relation = relation,
    images = images?.let {
        Character.Images(
            large = it.large ?: "",
            common = it.common ?: "",
            medium = it.medium ?: "",
            small = it.small ?: "",
            grid = it.grid ?: ""
        )
    },
    actors = actors?.map { actor ->
        Character.Actor(
            id = actor.id,
            name = actor.name,
            images = actor.images?.let {
                Character.Images(
                    large = it.large ?: "",
                    common = it.common ?: "",
                    medium = it.medium ?: "",
                    small = it.small ?: "",
                    grid = it.grid ?: ""
                )
            }
        )
    } ?: emptyList()
)
