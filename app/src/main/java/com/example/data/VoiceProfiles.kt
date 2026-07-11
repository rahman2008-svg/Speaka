package com.example.data

import java.util.Locale

data class AppLanguage(
    val code: String,       // unique id, e.g. "en_US", "bn_BD"
    val displayName: String, // e.g. "English (US)"
    val flag: String,       // flag emoji, e.g. "🇺🇸"
    val locale: Locale
)

enum class VoiceGender {
    MALE, FEMALE
}

data class AppVoice(
    val id: String,         // unique id, e.g. "en_us_female_1"
    val name: String,       // e.g. "Ava"
    val languageCode: String, // references AppLanguage.code
    val gender: VoiceGender,
    val basePitch: Float = 1.0f,
    val baseRate: Float = 1.0f
)

object VoiceProfiles {
    val languages = listOf(
        AppLanguage("en_US", "English (United States)", "🇺🇸", Locale.US),
        AppLanguage("en_GB", "English (United Kingdom)", "🇬🇧", Locale.UK),
        AppLanguage("en_AU", "English (Australia)", "🇦🇺", Locale("en", "AU")),
        AppLanguage("en_IN", "English (India)", "🇮🇳", Locale("en", "IN")),
        AppLanguage("en_CA", "English (Canada)", "🇨🇦", Locale.CANADA),
        AppLanguage("en_NG", "English (Nigeria)", "🇳🇬", Locale("en", "NG")),
        
        AppLanguage("bn_BD", "Bengali (Bangladesh)", "🇧🇩", Locale("bn", "BD")),
        AppLanguage("bn_IN", "Bengali (India)", "bn-IN", Locale("bn", "IN")),
        AppLanguage("hi_IN", "Hindi (India)", "🇮🇳", Locale("hi", "IN")),
        
        AppLanguage("zh_CN", "Chinese (Mandarin China)", "🇨🇳", Locale.SIMPLIFIED_CHINESE),
        AppLanguage("zh_TW", "Chinese (Taiwan)", "🇹🇼", Locale.TRADITIONAL_CHINESE),
        AppLanguage("zh_HK", "Chinese (Cantonese HK)", "🇭🇰", Locale("zh", "HK")),
        
        AppLanguage("es_ES", "Spanish (Spain)", "🇪🇸", Locale("es", "ES")),
        AppLanguage("es_MX", "Spanish (Mexico)", "🇲🇽", Locale("es", "MX")),
        AppLanguage("es_US", "Spanish (United States)", "🇺🇸", Locale("es", "US")),
        AppLanguage("es_419", "Spanish (Latin America)", "🌎", Locale("es", "419")),
        
        AppLanguage("fr_FR", "French (France)", "🇫🇷", Locale.FRANCE),
        AppLanguage("fr_CA", "French (Canada)", "🇨🇦", Locale.CANADA_FRENCH),
        AppLanguage("fr_BE", "French (Belgium)", "🇧🇪", Locale("fr", "BE")),
        AppLanguage("fr_CH", "French (Switzerland)", "🇨🇭", Locale("fr", "CH")),
        
        AppLanguage("ar_SA", "Arabic (Saudi Arabia)", "🇸🇦", Locale("ar", "SA")),
        AppLanguage("ar_AE", "Arabic (UAE)", "🇦🇪", Locale("ar", "AE")),
        
        AppLanguage("pt_BR", "Portuguese (Brazil)", "🇧🇷", Locale("pt", "BR")),
        AppLanguage("pt_PT", "Portuguese (Portugal)", "🇵🇹", Locale("pt", "PT")),
        
        AppLanguage("de_DE", "German (Germany)", "🇩🇪", Locale.GERMANY),
        AppLanguage("de_AT", "German (Austria)", "🇦🇹", Locale("de", "AT")),
        AppLanguage("de_CH", "German (Switzerland)", "🇨🇭", Locale("de", "CH")),
        
        AppLanguage("nl_NL", "Dutch (Netherlands)", "🇳🇱", Locale("nl", "NL")),
        AppLanguage("nl_BE", "Dutch (Belgium)", "🇧🇪", Locale("nl", "BE"))
    )

    val voices = listOf(
        // === English US (4 voices) ===
        AppVoice("en_us_female_1", "Ava", "en_US", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("en_us_female_2", "Sophia", "en_US", VoiceGender.FEMALE, basePitch = 1.25f),
        AppVoice("en_us_male_1", "James", "en_US", VoiceGender.MALE, basePitch = 0.85f),
        AppVoice("en_us_male_2", "Oliver", "en_US", VoiceGender.MALE, basePitch = 0.75f),

        // === English UK (4 voices) ===
        AppVoice("en_gb_female_1", "Emma", "en_GB", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("en_gb_female_2", "Lily", "en_GB", VoiceGender.FEMALE, basePitch = 1.25f),
        AppVoice("en_gb_male_1", "William", "en_GB", VoiceGender.MALE, basePitch = 0.85f),
        AppVoice("en_gb_male_2", "George", "en_GB", VoiceGender.MALE, basePitch = 0.75f),

        // === English Australia (4 voices) ===
        AppVoice("en_au_female_1", "Matilda", "en_AU", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("en_au_female_2", "Chloe", "en_AU", VoiceGender.FEMALE, basePitch = 1.25f),
        AppVoice("en_au_male_1", "Jack", "en_AU", VoiceGender.MALE, basePitch = 0.85f),
        AppVoice("en_au_male_2", "Lachlan", "en_AU", VoiceGender.MALE, basePitch = 0.75f),

        // === English India (4 voices) ===
        AppVoice("en_in_female_1", "Priya", "en_IN", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("en_in_female_2", "Ananya", "en_IN", VoiceGender.FEMALE, basePitch = 1.22f),
        AppVoice("en_in_male_1", "Aarav", "en_IN", VoiceGender.MALE, basePitch = 0.85f),
        AppVoice("en_in_male_2", "Arjun", "en_IN", VoiceGender.MALE, basePitch = 0.78f),

        // === English Canada (4 voices) ===
        AppVoice("en_ca_female_1", "Maple", "en_CA", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("en_ca_female_2", "Zoe", "en_CA", VoiceGender.FEMALE, basePitch = 1.25f),
        AppVoice("en_ca_male_1", "Logan", "en_CA", VoiceGender.MALE, basePitch = 0.85f),
        AppVoice("en_ca_male_2", "Felix", "en_CA", VoiceGender.MALE, basePitch = 0.75f),

        // === English Nigeria (4 voices) ===
        AppVoice("en_ng_female_1", "Chioma", "en_NG", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("en_ng_female_2", "Amina", "en_NG", VoiceGender.FEMALE, basePitch = 1.25f),
        AppVoice("en_ng_male_1", "Chidi", "en_NG", VoiceGender.MALE, basePitch = 0.85f),
        AppVoice("en_ng_male_2", "Emeka", "en_NG", VoiceGender.MALE, basePitch = 0.75f),

        // === Bengali Bangladesh (4 voices) ===
        AppVoice("bn_bd_female_1", "Oishee", "bn_BD", VoiceGender.FEMALE, basePitch = 1.18f),
        AppVoice("bn_bd_female_2", "Tasnim", "bn_BD", VoiceGender.FEMALE, basePitch = 1.26f),
        AppVoice("bn_bd_male_1", "Saimum", "bn_BD", VoiceGender.MALE, basePitch = 0.84f),
        AppVoice("bn_bd_male_2", "Fahim", "bn_BD", VoiceGender.MALE, basePitch = 0.75f),

        // === Bengali India (4 voices) ===
        AppVoice("bn_in_female_1", "Aditi", "bn_IN", VoiceGender.FEMALE, basePitch = 1.18f),
        AppVoice("bn_in_female_2", "Sneha", "bn_IN", VoiceGender.FEMALE, basePitch = 1.26f),
        AppVoice("bn_in_male_1", "Dev", "bn_IN", VoiceGender.MALE, basePitch = 0.84f),
        AppVoice("bn_in_male_2", "Subho", "bn_IN", VoiceGender.MALE, basePitch = 0.75f),

        // === Hindi India (4 voices) ===
        AppVoice("hi_in_female_1", "Diya", "hi_IN", VoiceGender.FEMALE, basePitch = 1.18f),
        AppVoice("hi_in_female_2", "Kavya", "hi_IN", VoiceGender.FEMALE, basePitch = 1.26f),
        AppVoice("hi_in_male_1", "Rahul", "hi_IN", VoiceGender.MALE, basePitch = 0.85f),
        AppVoice("hi_in_male_2", "Amit", "hi_IN", VoiceGender.MALE, basePitch = 0.75f),

        // === Chinese China (2 voices) ===
        AppVoice("zh_cn_female", "Meilin", "zh_CN", VoiceGender.FEMALE, basePitch = 1.18f),
        AppVoice("zh_cn_male", "Zhang", "zh_CN", VoiceGender.MALE, basePitch = 0.82f),

        // === Chinese Taiwan (2 voices) ===
        AppVoice("zh_tw_female", "Ting", "zh_TW", VoiceGender.FEMALE, basePitch = 1.18f),
        AppVoice("zh_tw_male", "Chen", "zh_TW", VoiceGender.MALE, basePitch = 0.82f),

        // === Chinese Hong Kong (2 voices) ===
        AppVoice("zh_hk_female", "Wing-Yee", "zh_HK", VoiceGender.FEMALE, basePitch = 1.18f),
        AppVoice("zh_hk_male", "Ka-Ho", "zh_HK", VoiceGender.MALE, basePitch = 0.82f),

        // === Spanish Spain (2 voices) ===
        AppVoice("es_es_female", "Lucia", "es_ES", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("es_es_male", "Mateo", "es_ES", VoiceGender.MALE, basePitch = 0.85f),

        // === Spanish Mexico (2 voices) ===
        AppVoice("es_mx_female", "Sofia", "es_MX", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("es_mx_male", "Diego", "es_MX", VoiceGender.MALE, basePitch = 0.85f),

        // === Spanish US (2 voices) ===
        AppVoice("es_us_female", "Camila", "es_US", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("es_us_male", "Lucas", "es_US", VoiceGender.MALE, basePitch = 0.85f),

        // === Spanish Latin America (2 voices) ===
        AppVoice("es_419_female", "Valentina", "es_419", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("es_419_male", "Sebastian", "es_419", VoiceGender.MALE, basePitch = 0.85f),

        // === French France (2 voices) ===
        AppVoice("fr_fr_female", "Chloe", "fr_FR", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("fr_fr_male", "Lucas", "fr_FR", VoiceGender.MALE, basePitch = 0.85f),

        // === French Canada (2 voices) ===
        AppVoice("fr_ca_female", "Amelie", "fr_CA", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("fr_ca_male", "Antoine", "fr_CA", VoiceGender.MALE, basePitch = 0.85f),

        // === French Belgium (2 voices) ===
        AppVoice("fr_be_female", "Manon", "fr_BE", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("fr_be_male", "Jules", "fr_BE", VoiceGender.MALE, basePitch = 0.85f),

        // === French Switzerland (2 voices) ===
        AppVoice("fr_ch_female", "Elise", "fr_CH", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("fr_ch_male", "Noah", "fr_CH", VoiceGender.MALE, basePitch = 0.85f),

        // === Arabic Saudi Arabia (2 voices) ===
        AppVoice("ar_sa_female", "Fatimah", "ar_SA", VoiceGender.FEMALE, basePitch = 1.20f),
        AppVoice("ar_sa_male", "Omar", "ar_SA", VoiceGender.MALE, basePitch = 0.80f),

        // === Arabic UAE (2 voices) ===
        AppVoice("ar_ae_female", "Reem", "ar_AE", VoiceGender.FEMALE, basePitch = 1.20f),
        AppVoice("ar_ae_male", "Zayed", "ar_AE", VoiceGender.MALE, basePitch = 0.80f),

        // === Portuguese Brazil (2 voices) ===
        AppVoice("pt_br_female", "Mariana", "pt_BR", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("pt_br_male", "Thiago", "pt_BR", VoiceGender.MALE, basePitch = 0.85f),

        // === Portuguese Portugal (2 voices) ===
        AppVoice("pt_pt_female", "Beatriz", "pt_PT", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("pt_pt_male", "Joao", "pt_PT", VoiceGender.MALE, basePitch = 0.85f),

        // === German Germany (2 voices) ===
        AppVoice("de_de_female", "Leonie", "de_DE", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("de_de_male", "Jonas", "de_DE", VoiceGender.MALE, basePitch = 0.85f),

        // === German Austria (2 voices) ===
        AppVoice("de_at_female", "Sarah", "de_AT", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("de_at_male", "Lukas", "de_AT", VoiceGender.MALE, basePitch = 0.85f),

        // === German Switzerland (2 voices) ===
        AppVoice("de_ch_female", "Mia", "de_CH", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("de_ch_male", "David", "de_CH", VoiceGender.MALE, basePitch = 0.85f),

        // === Dutch Netherlands (2 voices) ===
        AppVoice("nl_nl_female", "Fenna", "nl_NL", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("nl_nl_male", "Bram", "nl_NL", VoiceGender.MALE, basePitch = 0.85f),

        // === Dutch Belgium (2 voices) ===
        AppVoice("nl_be_female", "Amber", "nl_BE", VoiceGender.FEMALE, basePitch = 1.15f),
        AppVoice("nl_be_male", "Milan", "nl_BE", VoiceGender.MALE, basePitch = 0.85f)
    )
}
