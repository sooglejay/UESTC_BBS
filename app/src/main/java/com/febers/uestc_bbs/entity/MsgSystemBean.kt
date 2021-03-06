package com.febers.uestc_bbs.entity

class MsgSystemBean : MsgBaseBean() {

    var rs: Int = 0
    var errcode: String? = null
    var head: HeadBean? = null
    var body: BodyBean? = null
    var page: Int = 0
    var has_next: Int = 0
    var total_num: Int = 0
    var list: List<*>? = null

    class HeadBean {
        /**
         * errCode : 00000000
         * errInfo : 调用成功,没有任何错误
         * version : 2.6.1.7
         * alert : 0
         */

        var errCode: String? = null
        var errInfo: String? = null
        var version: String? = null
        var alert: Int = 0
    }

    class BodyBean {
        /**
         * externInfo : {"padding":""}
         * data : [{"dateline":"1516783261000","type":"system","note":"坏消息，坏消息，萌萌熊 看你不顺眼了,试图给你下头像变猪头的诅咒，不过这次他没成功。不知道他会不会再来一次的哦。想诅咒TA?点击这里试试吧","fromId":0,"fromIdType":"","author":"","authorId":0,"authorAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","actions":[]},{"dateline":"1514121800000","type":"system","note":"您的用户组升级为 河蟹 (Lv.3)   看看我能做什么 \u203a","fromId":0,"fromIdType":"changeusergroup","author":"","authorId":0,"authorAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","actions":[]},{"dateline":"1511485574000","type":"system","note":"11月25日线下活动通告！11月25日下午2点的开幕式改在活动中心1楼咖啡厅进行！！！到时候有大蛋糕吃！！！大家先过来看看，吃吃，再听听游戏的具体规则！我们不见不散！","fromId":0,"fromIdType":"sendnotice","author":"","authorId":0,"authorAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","actions":[]},{"dateline":"1511449182000","type":"system","note":"十年同舟共济，一路河畔有你清河畔今年十岁了！在这十年里，河畔陪伴着大家走过风风雨雨、河畔分享着大家的喜怒哀乐、河畔陪伴着大家一起成长！戳戳这里：http://bbs.uestc.edu.cn/tenth_anniversary/index.php，看看自己与河畔有着怎样的故事~送祝福给河畔有神秘加成哦~","fromId":0,"fromIdType":"sendnotice","author":"","authorId":0,"authorAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","actions":[]},{"dateline":"1510217118000","type":"system","note":"您的用户组升级为 虾米 (Lv.2)   看看我能做什么 \u203a","fromId":0,"fromIdType":"changeusergroup","author":"","authorId":0,"authorAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","actions":[]},{"dateline":"1471955234000","type":"system","note":"尊敬的四条眉毛，您已经注册成为清水河畔－电子科技大学官方论坛的会员，请你务必阅读新手导航以了解河畔。点此下载手机客户端如果您有什么疑问可以联系管理员，Email: uestcbbs@163.com。\r\n\r\n\r\n清水河畔－电子科技大学官方论坛\r\n2016-8-23 20:27","fromId":0,"fromIdType":"welcomemsg","author":"","authorId":0,"authorAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","actions":[]}]
         */

        var externInfo: ExternInfoBean? = null
        var data: List<DataBean>? = null

        class ExternInfoBean {
            /**
             * padding :
             */

            var padding: String? = null
        }

        class DataBean {
            /**
             * dateline : 1516783261000
             * type : system
             * note : 坏消息，坏消息，萌萌熊 看你不顺眼了,试图给你下头像变猪头的诅咒，不过这次他没成功。不知道他会不会再来一次的哦。想诅咒TA?点击这里试试吧
             * fromId : 0
             * fromIdType :
             * author :
             * authorId : 0
             * authorAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle
             * actions : []
             */

            var dateline: String? = null
            var type: String? = null
            var note: String? = null
            var fromId: Int = 0
            var fromIdType: String? = null
            var author: String? = null
            var authorId: Int = 0
            var authorAvatar: String? = null
            var actions: List<*>? = null
        }
    }
}
