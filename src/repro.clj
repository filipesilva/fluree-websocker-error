(ns repro
  (:require [fluree.db.api :as fdb]))


(def ledger "events/log")
(def conn (fdb/connect "http://localhost:8090"))
(def schema-tx [{:_id              :_collection
                 :_collection/name :event
                 :_collection/doc  "Athens semantic events."}
                {:_id               :_predicate
                 :_predicate/name   :event/id
                 :_predicate/doc    "A globally unique event id."
                 :_predicate/unique true
                 :_predicate/type   :string}
                {:_id             :_predicate
                 :_predicate/name :event/data
                 :_predicate/doc  "Event data serialized as an EDN string."
                 :_predicate/type :string}])


(defn tx-size [size]
  (println size "bytes event data")
  [{:_id        :event
    :event/id   (str "size-" size)
    :event/data (repeat size "a")}])


(defn repro
  []

  (when (empty? @(fdb/ledger-info conn ledger))
    ;; Create ledger
    @(fdb/new-ledger conn ledger)
    (fdb/wait-for-ledger-ready conn ledger)

    ;; Transact schema
    @(fdb/transact conn ledger schema-tx))


  ;; Add some event in growing size.

  @(fdb/transact conn ledger (tx-size 128))
  @(fdb/transact conn ledger (tx-size 512))
  @(fdb/transact conn ledger (tx-size 1024)) ;; fails here, but still writes to ledger
  #_@(fdb/transact conn ledger (tx-size 5120))


  (fdb/close conn))

(defn -main
  [_]
  (repro))

