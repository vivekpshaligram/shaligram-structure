package com.codestracture.data.manager.resource

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.codestracture.R
import com.codestracture.data.manager.preference.PreferenceManager

class ResourceManger(
    val context: Context,
    original: Resources,
    private val preferenceManager: PreferenceManager
) : Resources(original.assets, original.displayMetrics, original.configuration) {

    private val defaultColor = ContextCompat.getColor(context, R.color.colorPrimary)
    private val defaultSecondColor = ContextCompat.getColor(context, R.color.colorSecondary)
    private val buttonTitleColor = ContextCompat.getColor(context, R.color.black)

    private var firstColor =
        preferenceManager.getPrimaryColor()?.let {
            Color.parseColor(it)
        } ?: defaultColor

    private var secondColor =
        preferenceManager.getSecondaryColor()?.let {
            Color.parseColor(it)
        } ?: defaultSecondColor

    private var buttonTextColor =
        preferenceManager.getButtonTextColor()?.let {
            Color.parseColor(it)
        } ?: buttonTitleColor

    fun init() {
        firstColor = preferenceManager.getPrimaryColor()?.let {
            Color.parseColor(it)
        } ?: defaultColor
        secondColor = preferenceManager.getSecondaryColor()?.let {
            Color.parseColor(it)
        } ?: defaultSecondColor
        buttonTextColor = preferenceManager.getButtonTextColor()?.let {
            Color.parseColor(it)
        } ?: buttonTitleColor
    }

    fun getThemeColor(id: Int): Int {
        return when (getResourceEntryName(id)) {
            "firstColor" -> firstColor
            "colorPrimary" -> firstColor
            "colorSecondary" -> secondColor
            "buttonTitleColor" -> buttonTextColor
            else -> defaultColor
        }
    }

//    fun getThemeDrawable(id: Int): Drawable {
//        return when (getResourceEntryName(id)) {
//            "button_bg" -> context.getGradientDrawable(firstColor, secondColor)
//            "button_bg_5dp" -> context.getGradientDrawableWith5dp(firstColor, secondColor)
//            "button_bg_3dp" -> context.getGradientDrawableWith3dp(firstColor, secondColor)
//            "selected_pink_item_indicator", "pink_rounded_indicator",
//            "bg_rectangle_primary", "bg_rectangle_primary_small",
//            "ic_mail", "ic_call",
//            "ic_rules", "selected_blue_item_indicator",
//            "ic_scan", "ic_message","ic_user_verification",
//            "ic_radio_select", "color_cursor", "ic_bell_help","ic_calendar_rental","ic_map_pin" -> {
//                ContextCompat.getDrawable(context, id)!!.apply {
//                    colorFilter = PorterDuffColorFilter(firstColor, PorterDuff.Mode.SRC_ATOP)
//                }
//            }
//            "ic_wallet", "ic_wallet_2", "ic_add_color",
//            "ic_faq_pink", "ic_switch_on",
//            "ic_user_add_profile", "ic_user", "e_ride_pass",
//            "ic_promo", "ic_drink_drive_1",
//            "ic_drink_drive_2", "ic_drink_drive_3",
//            "ic_promo_invite", "ic_radio_check","ic_radio_check_pass", "ic_rectangle_infowindow",
//            "ic_infowindow_arrow", "ic_round_check" -> {
//                context.getDrawablePathColorFilter(id, defaultColor, firstColor)
//            }
//            "promo_no_record", "docking_station_icon",
//            "contact_us_icon", "no_fleet_icon",
//            "no_record", "refer_earn_icon",
//            "drawer_check_icon", "error_info_icon",
//            "drawer_failed_icon", "enable_location_icon",
//            "security_test_icon", "reservation_icon",
//            "keep_view_icon", "ride_pass_logo_icon","ride_pass_logo_icon_1" ->
//                context.getDrawablePathColorFilterSvg(id, joyrideDefaultColor, firstColor)
//            else -> ContextCompat.getDrawable(context, id)!!
//        }
//    }
//
//    fun getThemeDrawableByShapeId(id: Int, shapeId: Int): Drawable {
//        return context.getDrawableShapeColorFilter(id, shapeId, firstColor)
//    }
//    fun getThemeDrawableByShapeIdForDisable(id: Int, shapeId: Int, firstColor: Int): Drawable {
//        return context.getDrawableShapeColorFilter(id, shapeId, firstColor)
//    }
}