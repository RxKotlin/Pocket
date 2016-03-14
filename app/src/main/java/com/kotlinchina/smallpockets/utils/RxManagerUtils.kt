package com.kotlinchina.smallpockets.utils

import rx.Subscription
import rx.subscriptions.CompositeSubscription

object RxManagerUtils {
    /**
     * 去掉订阅，因为每当subcribe的时候就会生成一个Subcription引用，Observable会持有这个引用，所以为了避免内存溢出要在适合的地方释放
     * @param subscription
     * * 目前发现两种方法：
     * * 1、使用开源的RxLifeCycle
     * * 2、使用CompositeSubscription 在合适的地方释放 ，需要注意一点它释放以后要，重新生成才可以使用
     */
    fun unSubscribe(subscription: Subscription?) {
        if (subscription != null && subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
    }

    /**
     * 且来管得Subscrption的
     * @param compositeSubscription
     * *
     * @return
     */
    fun getNewCompositeSubIfUnsubscribed(compositeSubscription: CompositeSubscription?): CompositeSubscription {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed) {
            return CompositeSubscription()
        }
        return CompositeSubscription()

    }
}
