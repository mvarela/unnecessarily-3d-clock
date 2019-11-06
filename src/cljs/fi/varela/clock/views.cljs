(ns fi.varela.clock.views
  (:require
   [re-frame.core :as re-frame]
   [fi.varela.clock.subs :as subs]
   [cljs.pprint :as pprint]))


(defn main-panel []
    [:div
     [:h1 "Unnecessarily-3d clock"]
     [:h2 "Fun with threeagent and re-frame"]])


(defn background [material-props]
  (let [props (merge {:color  "white"
                      :opacity 0.8
                      :transparent true}
                     material-props)]
    [:object
     [:cylinder {:position [0 0 -14]
                 :radial-segments 60
                 :radius-top 11
                 :radius-bottom 13.5
                 :height 1
                 :open-ended? false
                 :rotation [(/ js/Math.PI 2.0) 0 0]
                 :material props}]]))

(defn seconds-hand []
  (let [seconds (re-frame/subscribe [::subs/time-seconds+ms])
        angle (subs/hand-angle @seconds)
        length 5.5
        [pos-x pos-y] (subs/hand-offsets length angle)]
    [:object {:position [pos-x pos-y +3]
              :rotation [0 0 angle]}
     [:box {:position [0  0 -10]
            :width 0.1
            :height length
            :depth 0.2
            :material {:color  "black"
                       :opacity 0.7
                       :transparent true}}]
     [:cone {:position [0 (* 0.55 length) -10]
             :radius 0.45
             :height 0.8
             :material {:color  "red"
                        :opacity 0.5
                        :transparent true}}]]))

(defn hand [material-props unit-key length]
  (let [props (merge {:color "black"
                      :opacity 0.8
                      :transparent true} material-props)
        units (re-frame/subscribe [unit-key])
        angle (subs/hand-angle @units)
        [pos-x pos-y] (subs/hand-offsets length angle)]
        [:object {:position [pos-x pos-y]
                  :rotation [0 0 angle]}
         [:box {:position [0 0 -10.5]
                :width 0.4
                :height length
                :depth 0.1
                :material props}]]))

(defn ticks [props-in radius length pos-z]
  (let [props (merge {:color "black"
                      :opacity 0.8
                      :transparent true} props-in)
        positions (mapv (fn[i]
                          (let [angle (subs/hand-angle (* 5 i))
                                pos-x (* radius (js/Math.cos angle))
                                pos-y (* radius (js/Math.sin angle))]
                            [:box {:position [pos-x pos-y pos-z]
                                   :rotation [0 0 (+ (/ js/Math.PI 2) angle)]
                                   :width 0.2
                                   :depth 0.1
                                   :height length
                                   :material props}]))
                        (range 12))]
    `[:object
     ~@positions]))

(defn root []
  (let [seconds (re-frame/subscribe [::subs/time-seconds])
        seconds+ms (re-frame/subscribe [::subs/time-seconds+ms])]
    [:object {:position [ 0 0 -6]}
     [:directional-light {:intensity 0.9
                          :position [(* 50 (js/Math.sin @seconds+ms))
                                     (* 50 (js/Math.cos @seconds+ms)) 100]
                          :cast-shadow true}]
     (background {:color "#373664"
                  :opacity 1
                  :transparent false})
     (seconds-hand)
     (hand {:color "#eead0e"
            :opacity 0.7} ::subs/time-hours-pos 6)
     (hand {:color "#888389"
            :opacity 0.9
            :transparent true} ::subs/time-minutes-pos 7.8)
     (ticks {:color "orange" :opacity 0.9} 8 1 -9.2)
     [:sphere {:position [0 0 -10]
               :radius 0.4
               :material {:color  "#8C5353"}}]]))
