(ns fi.varela.clock.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(defn pad [n]
  (if (< n 10)
    (str 0 n)
    n))

(re-frame/reg-sub
 ::time
 (fn[db]
   (apply str
          (interpose ":"
                     (map pad ((juxt :time-hours
                                     :time-minutes
                                     :time-seconds)
                               db))))))

(re-frame/reg-sub
 ::time-hours
 (fn[db]
   (:time-hours db)))

(re-frame/reg-sub
 ::time-hours-pos
 (fn[]
   (re-frame/subscribe [::time-hours]))
 (fn[hours]
   (* 5 hours)))

(re-frame/reg-sub
 ::time-minutes
 (fn[db]
   (:time-minutes db)))

(re-frame/reg-sub
 ::time-seconds
 (fn[db]
   (:time-seconds db)))

(re-frame/reg-sub
 ::time-seconds+ms
 (fn[db]
   (:time-seconds+ms db)))

(def pid2 (/ js/Math.PI 2))

(defn hand-angle [seconds]
    (* seconds (/ js/Math.PI -30.0)))

(defn hand-offsets [length angle]
  (let [xc (js/Math.cos (- angle pid2))
        ys (js/Math.sin (- angle pid2))
        len (/ length -2.0)]
   [(* len xc) (* len ys)]))
