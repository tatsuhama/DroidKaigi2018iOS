package sessiondetail

import entity.Session
import entity.toReadableTimeString
import kotlinx.cinterop.*
import libs.sd_setImageWithURL
import platform.CoreGraphics.CGRectGetHeight
import platform.Foundation.NSCoder
import platform.Foundation.NSURL
import platform.UIKit.*

@ExportObjCClass
class SessionDetailViewController(aDecoder: NSCoder) : UIViewController(aDecoder) {

    @ObjCOutlet lateinit var containerScroll: UIScrollView
    @ObjCOutlet lateinit var titleLabel: UILabel
    @ObjCOutlet lateinit var speakerAvatarImage: UIImageView
    @ObjCOutlet lateinit var speakerNameLabel: UILabel
    @ObjCOutlet lateinit var timeLabel: UILabel
    @ObjCOutlet lateinit var placeLabel: UILabel
    @ObjCOutlet lateinit var descriptionText: UILabel

    lateinit var sessionToShow: Session

    override fun initWithCoder(aDecoder: NSCoder): UIViewController? = initBy(SessionDetailViewController(aDecoder))

    override fun viewDidLoad() {
        super.viewDidLoad()

        // FIXME: It's not elegant.
        containerScroll.contentInset = UIEdgeInsetsMake(
                top = 64.0 /* = Status bar height + Navigation bar height  */,
                left = 0.0,
                bottom = tabBarController?.let { CGRectGetHeight(it.tabBar.frame) } ?: 0.0,
                right = 0.0
        )

        val session = sessionToShow
        when (session) {
            is Session.SpeechSession -> {
                titleLabel.text = session.title
                timeLabel.text = "DAY${session.dayNumber} / ${session.startTime.toReadableTimeString()} - ${session.endTime.toReadableTimeString()}"
                // TODO: Support displaying 2+ speakers.
                speakerAvatarImage.sd_setImageWithURL(NSURL(URLString = session.speakers.first().imageUrl))
                speakerNameLabel.text = session.speakers.first().name
                placeLabel.text = session.room.name
                descriptionText.text = session.desc
            }
            is Session.SpecialSession -> {
                titleLabel.text = session.title
                timeLabel.text = "DAY${session.dayNumber} / ${session.startTime.toReadableTimeString()} - ${session.endTime.toReadableTimeString()}"
                placeLabel.text = session.room?.name ?: ""
                // TODO: Hide speakers area and description area.
                descriptionText.text = ""
            }
        }
    }
}