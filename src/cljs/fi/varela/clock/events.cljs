(ns fi.varela.clock.events
  (:require
   [re-frame.core :as re-frame]
   [fi.varela.clock.db :as db]
   [cljs.pprint :as pprint]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-fx
 ::time
 [(re-frame/inject-cofx ::local-time)]
 (fn [{:keys [db time-hours time-minutes time-seconds time-seconds+ms]}]
   {:db (assoc db :time-hours time-hours
               :time-minutes time-minutes
               :time-seconds time-seconds
               :time-seconds+ms time-seconds+ms)}))

(re-frame/reg-cofx
 ::local-time
 (fn [cofx _]
   (let [now  (js/Date.)
         hh (.getHours now)
         mm (.getMinutes now)
         ss (.getSeconds now)
         ms (.getMilliseconds now)]
     (assoc cofx :time-hours hh
            :time-minutes mm
            :time-seconds ss
            :time-seconds+ms (+ ss (/ ms 1000.0))))))
