package com.example

data class BpsScaleInfo(
    val minPay: Int,
    val incrementPay: Int,
    val maxIncrements: Int
)

data class AllowanceRules(
    val hra30: Int,
    val hra45: Int,
    val hraSpecial: Int,
    val hraOrdinary: Int,
    val conveyance: Int,
    val gpFund: Int,
    val medical: Int = 1500,
    val entertainment: Int = 0,
    val sPost: Int = 0,
    val carMonetization: Int = 0
)

object BpsData {
    val years = listOf(1994, 2001, 2005, 2007, 2008, 2011, 2015, 2016, 2017, 2022)
    val grades = (1..22).map { String.format("%02d", it) }

    val payScales: Map<Int, Map<Int, BpsScaleInfo>> = mapOf(
        1 to mapOf(
            1994 to BpsScaleInfo(1245, 35, 30),
            2001 to BpsScaleInfo(1870, 55, 30),
            2005 to BpsScaleInfo(2150, 65, 30),
            2007 to BpsScaleInfo(2475, 75, 30),
            2008 to BpsScaleInfo(2970, 90, 30),
            2011 to BpsScaleInfo(4800, 150, 30),
            2015 to BpsScaleInfo(6210, 195, 30),
            2016 to BpsScaleInfo(7640, 240, 30),
            2017 to BpsScaleInfo(9130, 290, 30),
            2022 to BpsScaleInfo(13550, 430, 30)
        ),
        2 to mapOf(
            1994 to BpsScaleInfo(1275, 44, 30),
            2001 to BpsScaleInfo(1915, 65, 30),
            2005 to BpsScaleInfo(2200, 75, 30),
            2007 to BpsScaleInfo(2530, 85, 30),
            2008 to BpsScaleInfo(3035, 100, 30),
            2011 to BpsScaleInfo(4900, 170, 30),
            2015 to BpsScaleInfo(6335, 220, 30),
            2016 to BpsScaleInfo(7790, 275, 30),
            2017 to BpsScaleInfo(9310, 330, 30),
            2022 to BpsScaleInfo(13820, 490, 30)
        ),
        3 to mapOf(
            1994 to BpsScaleInfo(1320, 50, 30),
            2001 to BpsScaleInfo(1980, 75, 30),
            2005 to BpsScaleInfo(2275, 85, 30),
            2007 to BpsScaleInfo(2615, 100, 30),
            2008 to BpsScaleInfo(3140, 120, 30),
            2011 to BpsScaleInfo(5050, 200, 30),
            2015 to BpsScaleInfo(6535, 260, 30),
            2016 to BpsScaleInfo(8040, 325, 30),
            2017 to BpsScaleInfo(9610, 390, 30),
            2022 to BpsScaleInfo(14260, 580, 30)
        ),
        4 to mapOf(
            1994 to BpsScaleInfo(1360, 58, 30),
            2001 to BpsScaleInfo(2040, 85, 30),
            2005 to BpsScaleInfo(2345, 100, 30),
            2007 to BpsScaleInfo(2700, 115, 30),
            2008 to BpsScaleInfo(3240, 140, 30),
            2011 to BpsScaleInfo(5200, 230, 30),
            2015 to BpsScaleInfo(6730, 300, 30),
            2016 to BpsScaleInfo(8280, 370, 30),
            2017 to BpsScaleInfo(9900, 440, 30),
            2022 to BpsScaleInfo(14690, 660, 30)
        ),
        5 to mapOf(
            1994 to BpsScaleInfo(1400, 66, 30),
            2001 to BpsScaleInfo(2100, 100, 30),
            2005 to BpsScaleInfo(2415, 115, 30),
            2007 to BpsScaleInfo(2780, 135, 30),
            2008 to BpsScaleInfo(3340, 160, 30),
            2011 to BpsScaleInfo(5400, 260, 30),
            2015 to BpsScaleInfo(6985, 340, 30),
            2016 to BpsScaleInfo(8590, 420, 30),
            2017 to BpsScaleInfo(10260, 500, 30),
            2022 to BpsScaleInfo(15230, 750, 30)
        ),
        6 to mapOf(
            1994 to BpsScaleInfo(1440, 73, 30),
            2001 to BpsScaleInfo(2160, 110, 30),
            2005 to BpsScaleInfo(2485, 125, 30),
            2007 to BpsScaleInfo(2860, 145, 30),
            2008 to BpsScaleInfo(3430, 175, 30),
            2011 to BpsScaleInfo(5600, 290, 30),
            2015 to BpsScaleInfo(7235, 375, 30),
            2016 to BpsScaleInfo(8900, 470, 30),
            2017 to BpsScaleInfo(10620, 560, 30),
            2022 to BpsScaleInfo(15760, 840, 30)
        ),
        7 to mapOf(
            1994 to BpsScaleInfo(1480, 81, 30),
            2001 to BpsScaleInfo(2220, 120, 30),
            2005 to BpsScaleInfo(2555, 140, 30),
            2007 to BpsScaleInfo(2940, 160, 30),
            2008 to BpsScaleInfo(3530, 190, 30),
            2011 to BpsScaleInfo(5800, 320, 30),
            2015 to BpsScaleInfo(7490, 415, 30),
            2016 to BpsScaleInfo(9220, 510, 30),
            2017 to BpsScaleInfo(10990, 610, 30),
            2022 to BpsScaleInfo(16310, 910, 30)
        ),
        8 to mapOf(
            1994 to BpsScaleInfo(1540, 88, 30),
            2001 to BpsScaleInfo(2310, 130, 30),
            2005 to BpsScaleInfo(2655, 150, 30),
            2007 to BpsScaleInfo(3055, 175, 30),
            2008 to BpsScaleInfo(3665, 210, 30),
            2011 to BpsScaleInfo(6000, 350, 30),
            2015 to BpsScaleInfo(7750, 455, 30),
            2016 to BpsScaleInfo(9540, 560, 30),
            2017 to BpsScaleInfo(11380, 670, 30),
            2022 to BpsScaleInfo(16890, 1000, 30)
        ),
        9 to mapOf(
            1994 to BpsScaleInfo(1605, 97, 30),
            2001 to BpsScaleInfo(2410, 145, 30),
            2005 to BpsScaleInfo(2770, 165, 30),
            2007 to BpsScaleInfo(3185, 190, 30),
            2008 to BpsScaleInfo(3820, 230, 30),
            2011 to BpsScaleInfo(6200, 380, 30),
            2015 to BpsScaleInfo(8015, 495, 30),
            2016 to BpsScaleInfo(9860, 610, 30),
            2017 to BpsScaleInfo(11770, 730, 30),
            2022 to BpsScaleInfo(17470, 1090, 30)
        ),
        10 to mapOf(
            1994 to BpsScaleInfo(1660, 107, 30),
            2001 to BpsScaleInfo(2490, 160, 30),
            2005 to BpsScaleInfo(2865, 185, 30),
            2007 to BpsScaleInfo(3295, 215, 30),
            2008 to BpsScaleInfo(3955, 260, 30),
            2011 to BpsScaleInfo(6400, 420, 30),
            2015 to BpsScaleInfo(8275, 544, 30),
            2016 to BpsScaleInfo(10180, 670, 30),
            2017 to BpsScaleInfo(12160, 800, 30),
            2022 to BpsScaleInfo(18050, 1190, 30)
        ),
        11 to mapOf(
            1994 to BpsScaleInfo(1725, 116, 30),
            2001 to BpsScaleInfo(2590, 175, 30),
            2005 to BpsScaleInfo(2980, 200, 30),
            2007 to BpsScaleInfo(3430, 230, 30),
            2008 to BpsScaleInfo(4115, 275, 30),
            2011 to BpsScaleInfo(6600, 460, 30),
            2015 to BpsScaleInfo(8540, 595, 30),
            2016 to BpsScaleInfo(10510, 740, 30),
            2017 to BpsScaleInfo(12570, 880, 30),
            2022 to BpsScaleInfo(18650, 1310, 30)
        ),
        12 to mapOf(
            1994 to BpsScaleInfo(1830, 130, 30),
            2001 to BpsScaleInfo(2745, 195, 30),
            2005 to BpsScaleInfo(3155, 225, 30),
            2007 to BpsScaleInfo(3630, 260, 30),
            2008 to BpsScaleInfo(4355, 310, 30),
            2011 to BpsScaleInfo(7000, 500, 30),
            2015 to BpsScaleInfo(9055, 650, 30),
            2016 to BpsScaleInfo(11140, 800, 30),
            2017 to BpsScaleInfo(13320, 960, 30),
            2022 to BpsScaleInfo(19770, 1430, 30)
        ),
        13 to mapOf(
            1994 to BpsScaleInfo(1950, 144, 30),
            2001 to BpsScaleInfo(2925, 215, 30),
            2005 to BpsScaleInfo(3365, 245, 30),
            2007 to BpsScaleInfo(3870, 285, 30),
            2008 to BpsScaleInfo(4645, 340, 30),
            2011 to BpsScaleInfo(7500, 550, 30),
            2015 to BpsScaleInfo(9700, 715, 30),
            2016 to BpsScaleInfo(11930, 880, 30),
            2017 to BpsScaleInfo(14260, 1050, 30),
            2022 to BpsScaleInfo(21160, 1560, 30)
        ),
        14 to mapOf(
            1994 to BpsScaleInfo(2065, 161, 30),
            2001 to BpsScaleInfo(3100, 240, 30),
            2005 to BpsScaleInfo(3565, 275, 30),
            2007 to BpsScaleInfo(4100, 315, 30),
            2008 to BpsScaleInfo(4920, 380, 30),
            2011 to BpsScaleInfo(8000, 610, 30),
            2015 to BpsScaleInfo(10340, 790, 30),
            2016 to BpsScaleInfo(12720, 980, 30),
            2017 to BpsScaleInfo(15180, 1170, 30),
            2022 to BpsScaleInfo(22530, 1740, 30)
        ),
        15 to mapOf(
            1994 to BpsScaleInfo(2190, 177, 30),
            2001 to BpsScaleInfo(3285, 265, 30),
            2005 to BpsScaleInfo(3780, 305, 30),
            2007 to BpsScaleInfo(4350, 350, 30),
            2008 to BpsScaleInfo(5220, 420, 30),
            2011 to BpsScaleInfo(8500, 700, 30),
            2015 to BpsScaleInfo(10985, 905, 30),
            2016 to BpsScaleInfo(13510, 1120, 30),
            2017 to BpsScaleInfo(16120, 1330, 30),
            2022 to BpsScaleInfo(23920, 1980, 30)
        ),
        16 to mapOf(
            1994 to BpsScaleInfo(2535, 197, 30),
            2001 to BpsScaleInfo(3805, 295, 30),
            2005 to BpsScaleInfo(4375, 340, 30),
            2007 to BpsScaleInfo(5050, 390, 30),
            2008 to BpsScaleInfo(6060, 470, 30),
            2011 to BpsScaleInfo(10000, 800, 30),
            2015 to BpsScaleInfo(12910, 1035, 30),
            2016 to BpsScaleInfo(15880, 1280, 30),
            2017 to BpsScaleInfo(18910, 1520, 30),
            2022 to BpsScaleInfo(28070, 2260, 30)
        ),
        17 to mapOf(
            1994 to BpsScaleInfo(3880, 290, 20),
            2001 to BpsScaleInfo(6210, 465, 20),
            2005 to BpsScaleInfo(7140, 535, 20),
            2007 to BpsScaleInfo(8210, 615, 20),
            2008 to BpsScaleInfo(9850, 740, 20),
            2011 to BpsScaleInfo(16000, 1200, 20),
            2015 to BpsScaleInfo(20680, 1555, 20),
            2016 to BpsScaleInfo(25440, 1930, 20),
            2017 to BpsScaleInfo(30370, 2300, 20),
            2022 to BpsScaleInfo(45070, 3420, 20)
        ),
        18 to mapOf(
            1994 to BpsScaleInfo(5085, 366, 20),
            2001 to BpsScaleInfo(8135, 585, 20),
            2005 to BpsScaleInfo(9355, 675, 20),
            2007 to BpsScaleInfo(10760, 775, 20),
            2008 to BpsScaleInfo(12910, 930, 20),
            2011 to BpsScaleInfo(20000, 1500, 20),
            2015 to BpsScaleInfo(25940, 1950, 20),
            2016 to BpsScaleInfo(31890, 2400, 20),
            2017 to BpsScaleInfo(38350, 2870, 20),
            2022 to BpsScaleInfo(56880, 4260, 20)
        ),
        19 to mapOf(
            1994 to BpsScaleInfo(7750, 385, 20),
            2001 to BpsScaleInfo(12400, 615, 20),
            2005 to BpsScaleInfo(14260, 705, 20),
            2007 to BpsScaleInfo(16400, 810, 20),
            2008 to BpsScaleInfo(19680, 970, 20),
            2011 to BpsScaleInfo(31000, 1600, 20),
            2015 to BpsScaleInfo(40155, 2075, 20),
            2016 to BpsScaleInfo(49370, 2560, 20),
            2017 to BpsScaleInfo(59210, 3050, 20),
            2022 to BpsScaleInfo(87840, 4530, 20)
        ),
        20 to mapOf(
            1994 to BpsScaleInfo(9195, 440, 14),
            2001 to BpsScaleInfo(14710, 950, 14),
            2005 to BpsScaleInfo(16915, 1095, 14),
            2007 to BpsScaleInfo(19455, 1260, 14),
            2008 to BpsScaleInfo(23345, 1510, 14),
            2011 to BpsScaleInfo(36000, 2350, 14),
            2015 to BpsScaleInfo(46705, 3050, 14),
            2016 to BpsScaleInfo(57410, 3750, 14),
            2017 to BpsScaleInfo(69090, 4510, 14),
            2022 to BpsScaleInfo(102470, 6690, 14)
        ),
        21 to mapOf(
            1994 to BpsScaleInfo(10190, 545, 14),
            2001 to BpsScaleInfo(16305, 1070, 14),
            2005 to BpsScaleInfo(18750, 1230, 14),
            2007 to BpsScaleInfo(21565, 1415, 14),
            2008 to BpsScaleInfo(25880, 1700, 14),
            2011 to BpsScaleInfo(40000, 2600, 14),
            2015 to BpsScaleInfo(51885, 3375, 14),
            2016 to BpsScaleInfo(63780, 4150, 14),
            2017 to BpsScaleInfo(76720, 5000, 14),
            2022 to BpsScaleInfo(113790, 7420, 14)
        ),
        22 to mapOf(
            1994 to BpsScaleInfo(10900, 610, 14),
            2001 to BpsScaleInfo(17440, 1250, 14),
            2005 to BpsScaleInfo(20055, 1440, 14),
            2007 to BpsScaleInfo(23065, 1655, 14),
            2008 to BpsScaleInfo(27680, 1985, 14),
            2011 to BpsScaleInfo(43000, 3050, 14),
            2015 to BpsScaleInfo(55755, 3960, 14),
            2016 to BpsScaleInfo(68540, 4870, 14),
            2017 to BpsScaleInfo(82380, 5870, 14),
            2022 to BpsScaleInfo(122190, 8710, 14)
        )
    )

    val allowanceRules: Map<Int, AllowanceRules> = mapOf(
        1 to AllowanceRules(hra30 = 1337, hra45 = 2005, hraSpecial = 800, hraOrdinary = 496, conveyance = 1700, gpFund = 600),
        2 to AllowanceRules(hra30 = 1366, hra45 = 2049, hraSpecial = 800, hraOrdinary = 496, conveyance = 1700, gpFund = 1060),
        3 to AllowanceRules(hra30 = 1413, hra45 = 2120, hraSpecial = 800, hraOrdinary = 496, conveyance = 1700, gpFund = 1150),
        4 to AllowanceRules(hra30 = 1458, hra45 = 2187, hraSpecial = 800, hraOrdinary = 496, conveyance = 1700, gpFund = 1230),
        5 to AllowanceRules(hra30 = 1503, hra45 = 2255, hraSpecial = 880, hraOrdinary = 624, conveyance = 1840, gpFund = 1330),
        6 to AllowanceRules(hra30 = 1544, hra45 = 2315, hraSpecial = 880, hraOrdinary = 624, conveyance = 1840, gpFund = 1420),
        7 to AllowanceRules(hra30 = 1589, hra45 = 2383, hraSpecial = 880, hraOrdinary = 624, conveyance = 1840, gpFund = 1500),
        8 to AllowanceRules(hra30 = 1649, hra45 = 2474, hraSpecial = 880, hraOrdinary = 624, conveyance = 1840, gpFund = 1600),
        9 to AllowanceRules(hra30 = 1719, hra45 = 2579, hraSpecial = 880, hraOrdinary = 624, conveyance = 1840, gpFund = 1700),
        10 to AllowanceRules(hra30 = 1780, hra45 = 2670, hraSpecial = 880, hraOrdinary = 624, conveyance = 1840, gpFund = 1800),
        11 to AllowanceRules(hra30 = 1852, hra45 = 2778, hraSpecial = 880, hraOrdinary = 624, conveyance = 2720, gpFund = 1920),
        12 to AllowanceRules(hra30 = 1960, hra45 = 2940, hraSpecial = 1940, hraOrdinary = 1120, conveyance = 2720, gpFund = 3300),
        13 to AllowanceRules(hra30 = 2090, hra45 = 3135, hraSpecial = 1440, hraOrdinary = 1120, conveyance = 2720, gpFund = 3570),
        14 to AllowanceRules(hra30 = 2214, hra45 = 3321, hraSpecial = 1440, hraOrdinary = 1120, conveyance = 2720, gpFund = 3900),
        15 to AllowanceRules(hra30 = 2349, hra45 = 3524, hraSpecial = 1440, hraOrdinary = 1120, conveyance = 2720, gpFund = 4290),
        16 to AllowanceRules(hra30 = 2727, hra45 = 4091, hraSpecial = 1440, hraOrdinary = 1120, conveyance = 5000, gpFund = 4960),
        17 to AllowanceRules(hra30 = 4433, hra45 = 6649, hraSpecial = 2560, hraOrdinary = 2000, conveyance = 5000, gpFund = 6350),
        18 to AllowanceRules(hra30 = 5810, hra45 = 8714, hraSpecial = 2560, hraOrdinary = 2000, conveyance = 5000, gpFund = 7960),
        19 to AllowanceRules(hra30 = 8856, hra45 = 13284, hraSpecial = 3280, hraOrdinary = 2480, conveyance = 5000, gpFund = 10660, entertainment = 500),
        20 to AllowanceRules(hra30 = 10505, hra45 = 15758, hraSpecial = 2480, hraOrdinary = 2480, conveyance = 0, gpFund = 11950, entertainment = 600, sPost = 1100, carMonetization = 65960),
        21 to AllowanceRules(hra30 = 11646, hra45 = 17469, hraSpecial = 4000, hraOrdinary = 2800, conveyance = 0, gpFund = 13260, entertainment = 700, sPost = 1200, carMonetization = 77430),
        22 to AllowanceRules(hra30 = 12456, hra45 = 18684, hraSpecial = 4800, hraOrdinary = 2800, conveyance = 0, gpFund = 14660, entertainment = 975, sPost = 1600, carMonetization = 95910)
    )
}
