package com.zxwork.bmg.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.zxwork.bmg.domain.model.Subject
import com.zxwork.bmg.domain.model.SubjectDetail
import com.zxwork.bmg.ui.theme.BmgTheme

import androidx.compose.foundation.lazy.rememberLazyListState

import com.zxwork.bmg.domain.model.Character
import com.zxwork.bmg.domain.model.Episode

@Preview(showBackground = true, name = "Subject Detail - Success")
@Composable
fun SubjectDetailPreview() {
    val scrollState = rememberLazyListState()
    
    val mockEpisodes = listOf(
        Episode(1, 1.0, 0, "Episode 1", "第一话", "2024-01-07", "24:00", ""),
        Episode(2, 2.0, 0, "Episode 2", "第二话", "2024-01-14", "24:00", ""),
        Episode(3, 3.0, 0, "Episode 3", "第三话", "2024-01-21", "24:00", "")
    )

    val mockCharacters = listOf(
        Character(
            id = 1,
            name = "市川京太郎",
            relation = "主角",
            images = Character.Images("", "", "", "", ""),
            actors = listOf(Character.Actor(1, "堀江瞬", null))
        ),
        Character(
            id = 2,
            name = "山田杏奈",
            relation = "主角",
            images = Character.Images("", "", "", "", ""),
            actors = listOf(Character.Actor(2, "羊宮妃那", null))
        )
    )

    val mockDetail = SubjectDetail(
        // ... (rest of detail)
        // ... (rest of detail)
        // ... (rest of details)
        id = 543360,
        type = 2,
        name = "僕の心のヤバイやつ 第2期",
        nameCn = "我内心的糟糕念头 第二季",
        summary = "市川京太郎是位由于有着重度中二病，而陷入了“杀掉处于班级金字塔顶端的绝世美女山田杏奈”的妄想中的阴角中学生。但是，在图书馆偶然观察山田后，市川发现了她令人意外的一面……",
        date = "2024-01-07",
        platform = "TV",
        images = Subject.Images(
            large = "",
            common = "",
            medium = "",
            small = "",
            grid = ""
        ),
        rating = SubjectDetail.Rating(
            score = 8.8,
            total = 12500,
            rank = 12
        ),
        collection = SubjectDetail.Collection(
            wish = 1200,
            collect = 8500,
            doing = 1500,
            onHold = 300,
            dropped = 100
        ),
        tags = listOf(
            SubjectDetail.Tag("校园", 1200),
            SubjectDetail.Tag("恋爱", 1100),
            SubjectDetail.Tag("狗粮", 950),
            SubjectDetail.Tag("中二病", 400),
            SubjectDetail.Tag("日常", 300)
        ),
        infobox = listOf(
            SubjectDetail.InfoBoxItem("中文名", "我内心的糟糕念头 第二季"),
            SubjectDetail.InfoBoxItem("话数", "13"),
            SubjectDetail.InfoBoxItem("放送开始", "2024年1月7日"),
            SubjectDetail.InfoBoxItem("导演", "赤城博昭"),
            SubjectDetail.InfoBoxItem("音乐", "牛尾憲輔")
        )
    )

    BmgTheme {
        SubjectDetailContent(
            subject = mockDetail, 
            scrollState = scrollState,
            episodes = mockEpisodes,
            characters = mockCharacters
        )
    }
}
