package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class JournalTheme(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconCode: String,
    val iconEmoji: String = "",
    val defaultPaper: PaperStyle,
    val defaultPalette: PaletteType,
    val accentColor: Color
) {
    CALM(
        id = "CALM",
        title = "Kalem (Coklat Susu & Blue)",
        subtitle = "Coklat susu hangat & soft blue yang menenangkan",
        iconCode = "coffee",
        defaultPaper = PaperStyle.DOTS,
        defaultPalette = PaletteType.CALM_LATTE,
        accentColor = Color(0xFFC59B76)
    ),
    FALL(
        id = "FALL",
        title = "Musim Gugur (Fall)",
        subtitle = "Hangat, dedaunan amber & aroma kayu manis",
        iconCode = "autumn_leaf",
        defaultPaper = PaperStyle.KRAFT,
        defaultPalette = PaletteType.AUTUMN,
        accentColor = AutumnAmber
    ),
    HALLOWEEN(
        id = "HALLOWEEN",
        title = "Halloween Magis",
        subtitle = "Misterius, labu lentera & malam berbintang",
        iconCode = "night_magic",
        defaultPaper = PaperStyle.DARK,
        defaultPalette = PaletteType.HALLOWEEN,
        accentColor = HalloweenOrange
    ),
    SNOW(
        id = "SNOW",
        title = "Salju Dingin (Snow)",
        subtitle = "Tenang, keping es & selimut wol putih",
        iconCode = "snowflake",
        defaultPaper = PaperStyle.DOTS,
        defaultPalette = PaletteType.SNOW,
        accentColor = SnowFrost
    ),
    SIMPLE(
        id = "SIMPLE",
        title = "Simple Minimalis",
        subtitle = "Lega, bersih, tenang tanpa riuh",
        iconCode = "clean_leaf",
        defaultPaper = PaperStyle.DOTS,
        defaultPalette = PaletteType.MINIMAL,
        accentColor = EarthCharcoal
    ),
    EARTH(
        id = "EARTH",
        title = "Bumi Alami (Earth)",
        subtitle = "Sage, tanah liat & keteduhan hutan",
        iconCode = "earth_plant",
        defaultPaper = PaperStyle.KRAFT,
        defaultPalette = PaletteType.EARTH,
        accentColor = EarthTerracotta
    ),
    PASTEL(
        id = "PASTEL",
        title = "Pastel Impian",
        subtitle = "Lembut, manis bak awan gula-gula",
        iconCode = "blossom",
        defaultPaper = PaperStyle.BLANK,
        defaultPalette = PaletteType.PASTEL,
        accentColor = PastelPink
    ),
    NEON(
        id = "NEON",
        title = "Neon Pop",
        subtitle = "Bersemangat, berani & penuh kilau",
        iconCode = "neon_spark",
        defaultPaper = PaperStyle.DARK,
        defaultPalette = PaletteType.NEON,
        accentColor = NeonCyan
    ),
    RANDOM(
        id = "RANDOM",
        title = "Bebas & Campur (Random)",
        subtitle = "Eksplorasi liar tanpa batasan tema",
        iconCode = "palette_brush",
        defaultPaper = PaperStyle.GRID,
        defaultPalette = PaletteType.PASTEL,
        accentColor = PastelLavender
    );

    companion object {
        fun fromId(id: String): JournalTheme = entries.find { it.id == id } ?: CALM
    }
}

enum class PaperStyle(
    val id: String,
    val title: String,
    val description: String,
    val lightPaperBg: Color,
    val darkPaperBg: Color,
    val lightPatternColor: Color,
    val darkPatternColor: Color
) {
    DOTS(
        id = "DOTS",
        title = "Titik (Dotted)",
        description = "Titik rapi berjarak lega",
        lightPaperBg = Color(0xFFF9F7F3),
        darkPaperBg = Color(0xFF222026),
        lightPatternColor = Color(0xFFD6CEBF),
        darkPatternColor = Color(0xFF3C3845)
    ),
    GRID(
        id = "GRID",
        title = "Kotak (Grid)",
        description = "Garis kotak halus simetris",
        lightPaperBg = Color(0xFFFAF8F5),
        darkPaperBg = Color(0xFF222026),
        lightPatternColor = Color(0xFFE2DCD2),
        darkPatternColor = Color(0xFF383540)
    ),
    LINES(
        id = "LINES",
        title = "Garis (Ruled)",
        description = "Garis lapang untuk tulisan",
        lightPaperBg = Color(0xFFFAF7F2),
        darkPaperBg = Color(0xFF232128),
        lightPatternColor = Color(0xFFDFD7CA),
        darkPatternColor = Color(0xFF383442)
    ),
    BLANK(
        id = "BLANK",
        title = "Polos (Blank)",
        description = "Kertas bersih tanpa motif",
        lightPaperBg = Color(0xFFFAF8F4),
        darkPaperBg = Color(0xFF201E25),
        lightPatternColor = Color.Transparent,
        darkPatternColor = Color.Transparent
    ),
    KRAFT(
        id = "KRAFT",
        title = "Vintage Kraft",
        description = "Kertas cokelat hangat klasik",
        lightPaperBg = Color(0xFFEFE4D2),
        darkPaperBg = Color(0xFF2A241F),
        lightPatternColor = Color(0xFFDDD0BD),
        darkPatternColor = Color(0xFF3D342C)
    ),
    DARK(
        id = "DARK",
        title = "Malam Gelap (Night)",
        description = "Kanvas gelap estetik kontras",
        lightPaperBg = Color(0xFF222129),
        darkPaperBg = Color(0xFF18171C),
        lightPatternColor = Color(0xFF383543),
        darkPatternColor = Color(0xFF2B2835)
    );

    companion object {
        fun fromId(id: String): PaperStyle = entries.find { it.id == id } ?: DOTS
    }
}

enum class PaletteType(
    val id: String,
    val title: String,
    val colors: List<Color>
) {
    CALM_LATTE(
        id = "CALM_LATTE",
        title = "Coklat Susu & Soft Blue",
        colors = listOf(
            Color(0xFFC59B76), // Coklat Susu
            Color(0xFFA2D2FF), // Soft Blue
            Color(0xFF8D5B4C), // Mocha
            Color(0xFFBDE0FE), // Powder Blue
            Color(0xFFDEBA9D), // Susu Hangat
            Color(0xFF8ECAE6)  // Sky Frost
        )
    ),
    EARTH(
        id = "EARTH",
        title = "Bumi (Earth)",
        colors = listOf(
            Color(0xFFC86D51), // Terracotta
            Color(0xFF6B8E7B), // Sage
            Color(0xFFDCC8B2), // Sand
            Color(0xFF5C4033), // Coffee Brown
            Color(0xFF8B5A2B), // Clay
            Color(0xFFA3B18A)  // Light Olive
        )
    ),
    PASTEL(
        id = "PASTEL",
        title = "Pastel Lembut",
        colors = listOf(
            Color(0xFFFFB7B2), // Baby Pink
            Color(0xFFD0C3F0), // Lavender
            Color(0xFFB5EAD7), // Mint
            Color(0xFFFFE699), // Butter
            Color(0xFFA0C4FF), // Sky Blue
            Color(0xFFFFDAC1)  // Peach
        )
    ),
    NEON(
        id = "NEON",
        title = "Neon Pop",
        colors = listOf(
            Color(0xFF00F0FF), // Cyan
            Color(0xFFFF2A85), // Pink
            Color(0xFF39FF14), // Lime
            Color(0xFF9D4EDD), // Purple
            Color(0xFFFFE500), // Yellow
            Color(0xFFFF5400)  // Orange
        )
    ),
    AUTUMN(
        id = "AUTUMN",
        title = "Gugur (Fall)",
        colors = listOf(
            Color(0xFFD97724),
            Color(0xFFBC4749),
            Color(0xFFE09F3E),
            Color(0xFF588157),
            Color(0xFF6F1D1B),
            Color(0xFFBB9457)
        )
    ),
    HALLOWEEN(
        id = "HALLOWEEN",
        title = "Halloween",
        colors = listOf(
            Color(0xFFF77F00),
            Color(0xFF7209B7),
            Color(0xFF52B788),
            Color(0xFF1E1A2B),
            Color(0xFFFF9E00),
            Color(0xFFB5179E)
        )
    ),
    SNOW(
        id = "SNOW",
        title = "Salju (Snow)",
        colors = listOf(
            Color(0xFF8ECAE6),
            Color(0xFF219EBC),
            Color(0xFF023E8A),
            Color(0xFFE2F3F8),
            Color(0xFF90E0EF),
            Color(0xFF5B7082)
        )
    ),
    MINIMAL(
        id = "MINIMAL",
        title = "Minimalis",
        colors = listOf(
            Color(0xFF2C2825),
            Color(0xFF6C757D),
            Color(0xFF9A8C98),
            Color(0xFFC9ADA7),
            Color(0xFF4A4E69),
            Color(0xFF22223B)
        )
    );

    companion object {
        fun fromId(id: String): PaletteType = entries.find { it.id == id } ?: CALM_LATTE
    }
}

data class StickerPreset(
    val id: String,
    val theme: JournalTheme,
    val stickerKey: String, // Original vector illustration key
    val name: String,
    val previewTint: Color = Color.Unspecified
)

data class WashiTapePreset(
    val id: String,
    val name: String,
    val patternStyle: String, // STRIPES, GINGHAM, POLKADOT, BOTANICAL, GRID, SOLID
    val primaryColorHex: Long,
    val secondaryColorHex: Long
)

data class StampPreset(
    val id: String,
    val title: String,
    val subtitle: String,
    val frameStyle: String // "BORDER_BOX", "OVAL_DOUBLE", "POSTAL_CIRCLE", "MINIMAL_BAR"
)

object JournalPresets {
    val STICKERS: List<StickerPreset> = listOf(
        // CALM
        StickerPreset("calm_cup", JournalTheme.CALM, "coffee_cup", "Cangkir Latte"),
        StickerPreset("calm_cloud", JournalTheme.CALM, "fluffy_cloud", "Awan Lembut"),
        StickerPreset("calm_sprig", JournalTheme.CALM, "botanical_sprig", "Ranting Eucalyptus"),
        StickerPreset("calm_daisy", JournalTheme.CALM, "daisy_flower", "Bunga Daisy"),
        StickerPreset("calm_polaroid", JournalTheme.CALM, "polaroid_photo", "Foto Polaroid"),
        StickerPreset("calm_seal", JournalTheme.CALM, "wax_seal", "Segel Lilin Antik"),

        // FALL
        StickerPreset("fall_maple", JournalTheme.FALL, "maple_leaf", "Daun Maple"),
        StickerPreset("fall_acorn", JournalTheme.FALL, "acorn", "Biji Acorn"),
        StickerPreset("fall_cup", JournalTheme.FALL, "coffee_cup", "Kopi Kayu Manis"),
        StickerPreset("fall_seal", JournalTheme.FALL, "wax_seal", "Segel Terracotta"),
        StickerPreset("fall_polaroid", JournalTheme.FALL, "polaroid_photo", "Polaroid Senja"),
        StickerPreset("fall_sprig", JournalTheme.FALL, "botanical_sprig", "Dedaunan Kering"),

        // HALLOWEEN
        StickerPreset("hw_pumpkin", JournalTheme.HALLOWEEN, "pumpkin", "Labu Lentera"),
        StickerPreset("hw_ghost", JournalTheme.HALLOWEEN, "ghost", "Hantu Ramah"),
        StickerPreset("hw_cat", JournalTheme.HALLOWEEN, "black_cat", "Kucing Hitam"),
        StickerPreset("hw_seal", JournalTheme.HALLOWEEN, "wax_seal", "Segel Magis"),
        StickerPreset("hw_sparkle", JournalTheme.HALLOWEEN, "sparkle_cluster", "Bintang Malam"),

        // SNOW
        StickerPreset("snow_flake", JournalTheme.SNOW, "snowflake", "Kristal Salju"),
        StickerPreset("snow_man", JournalTheme.SNOW, "snowman", "Boneka Salju"),
        StickerPreset("snow_cup", JournalTheme.SNOW, "coffee_cup", "Cokelat Panas"),
        StickerPreset("snow_cloud", JournalTheme.SNOW, "fluffy_cloud", "Awan Dingin"),
        StickerPreset("snow_sparkle", JournalTheme.SNOW, "sparkle_cluster", "Kilau Salju"),

        // SIMPLE
        StickerPreset("sm_sprig", JournalTheme.SIMPLE, "botanical_sprig", "Ranting Daun"),
        StickerPreset("sm_clip", JournalTheme.SIMPLE, "paperclip", "Klip Kertas"),
        StickerPreset("sm_daisy", JournalTheme.SIMPLE, "daisy_flower", "Bunga Daisy"),
        StickerPreset("sm_sparkle", JournalTheme.SIMPLE, "sparkle_cluster", "Kilau Halus"),
        StickerPreset("sm_cup", JournalTheme.SIMPLE, "coffee_cup", "Cangkir Teh"),
        StickerPreset("sm_seal", JournalTheme.SIMPLE, "wax_seal", "Cap Stempel"),

        // EARTH
        StickerPreset("earth_sprig", JournalTheme.EARTH, "botanical_sprig", "Eucalyptus Sage"),
        StickerPreset("earth_daisy", JournalTheme.EARTH, "daisy_flower", "Aster Liar"),
        StickerPreset("earth_acorn", JournalTheme.EARTH, "acorn", "Biji Hutan"),
        StickerPreset("earth_cup", JournalTheme.EARTH, "coffee_cup", "Teh Herbal"),
        StickerPreset("earth_polaroid", JournalTheme.EARTH, "polaroid_photo", "Foto Alam"),

        // PASTEL
        StickerPreset("ps_bow", JournalTheme.PASTEL, "ribbon_bow", "Pita Merah Muda"),
        StickerPreset("ps_strawberry", JournalTheme.PASTEL, "strawberry", "Stroberi Manis"),
        StickerPreset("ps_cloud", JournalTheme.PASTEL, "fluffy_cloud", "Awan Pastel"),
        StickerPreset("ps_butterfly", JournalTheme.PASTEL, "butterfly", "Kupu-kupu"),
        StickerPreset("ps_daisy", JournalTheme.PASTEL, "daisy_flower", "Bunga Sakura"),

        // NEON
        StickerPreset("neon_bolt", JournalTheme.NEON, "neon_bolt", "Petir Neon"),
        StickerPreset("neon_cassette", JournalTheme.NEON, "cassette_tape", "Kaset Musik Retro"),
        StickerPreset("neon_sparkle", JournalTheme.NEON, "sparkle_cluster", "Cahaya Neon"),
        StickerPreset("neon_polaroid", JournalTheme.NEON, "polaroid_photo", "Polaroid Kota")
    )

    val WASHI_TAPES: List<WashiTapePreset> = listOf(
        WashiTapePreset("wt_soft_blue_latte", "Soft Blue & Susu", "STRIPES", 0xFFA2D2FF, 0xFFC59B76),
        WashiTapePreset("wt_earth_gingham", "Terracotta Gingham", "GINGHAM", 0xFFC86D51, 0xFFEEDAC5),
        WashiTapePreset("wt_sage_dots", "Sage Polkadot", "POLKADOT", 0xFF6B8E7B, 0xFFFFFFFF),
        WashiTapePreset("wt_autumn_tartan", "Gugur Tartan", "STRIPES", 0xFFD97724, 0xFFBC4749),
        WashiTapePreset("wt_pastel_pink_dots", "Pink Polkadot", "POLKADOT", 0xFFFFB7B2, 0xFFFFF0F0),
        WashiTapePreset("wt_pastel_mint_grid", "Mint Grid", "GRID", 0xFFB5EAD7, 0xFF355E4E),
        WashiTapePreset("wt_lavender_stripes", "Lavender Manis", "STRIPES", 0xFFD0C3F0, 0xFFFFFFFF),
        WashiTapePreset("wt_neon_cyber", "Cyber Neon", "STRIPES", 0xFF00F0FF, 0xFFFF2A85),
        WashiTapePreset("wt_halloween_stripes", "Halloween Belang", "STRIPES", 0xFFF77F00, 0xFF1E1A2B),
        WashiTapePreset("wt_snow_ice_dots", "Kristal Salju", "POLKADOT", 0xFF8ECAE6, 0xFFFFFFFF),
        WashiTapePreset("wt_minimal_grid", "Monokrom Grid", "GRID", 0xFF2C2825, 0xFFE5DFD7)
    )

    // Stempel Pos & Cap Jurnal Tipografi Otentik (Tanpa Emoji)
    val STAMPS: List<StampPreset> = listOf(
        StampPreset("st_today", "HARI INI", "OFFICIAL ENTRY", "BORDER_BOX"),
        StampPreset("st_recorded", "RECORDED", "OURJOUR ARCHIVE", "OVAL_DOUBLE"),
        StampPreset("st_calm", "CALM & COZY", "NOTEBOOK MOMENT", "POSTAL_CIRCLE"),
        StampPreset("st_favorite", "FAVORITE", "SPECIAL MEMORY", "BORDER_BOX"),
        StampPreset("st_inspirasi", "INSPIRASI", "CREATIVE SPACE", "MINIMAL_BAR"),
        StampPreset("st_damai", "DAMAI", "PEACEFUL MIND", "OVAL_DOUBLE"),
        StampPreset("st_coffee", "WARM COFFEE", "CAFE VIBES", "POSTAL_CIRCLE"),
        StampPreset("st_gratitude", "BERSYUKUR", "DAILY GRATITUDE", "BORDER_BOX"),
        StampPreset("st_verified", "TERVERIFIKASI", "OURJOUR • 2026", "POSTAL_CIRCLE"),
        StampPreset("st_autumn", "AUTUMN CHILL", "COLLECTION", "MINIMAL_BAR")
    )
}
