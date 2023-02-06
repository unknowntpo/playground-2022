First, run this command to load `.so` file to extension directory specified by `pg_config --sharedir`

```
$ make install
```

Then, You need to modify this line in postgresql.conf to preload shared libs (require restarting postgres server).

```
shared_preload_libraries = 'plannerlog.so'	# (change requires restart)
```

To find the location of `postgres.conf`, use this command in `psql`:

```
unknowntpo=# show config_file;
                config_file                 
--------------------------------------------
 /opt/homebrew/var/postgres/postgresql.conf
(1 row)
```

Finally, execute this command to create extension:

```
unknowntpo=# create extension plannerlog;
CREATE EXTENSION
```

Let's examine the output of `SELECT 1` SQL Query:

```
unknowntpo=# select 1;
NOTICE:  Executing statement: {PLANNEDSTMT :commandType 1 :queryId 0 :hasReturning false :hasModifyingCTE false :canSetTag true :transientPlan false :dependsOnRole false :parallelModeNeeded false :jitFlags 0 :planTree {RESULT :startup_cost 0.00 :total_cost 0.01 :plan_rows 1 :plan_width 4 :parallel_aware false :parallel_safe false :async_capable false :plan_node_id 0 :targetlist ({TARGETENTRY :expr {CONST :consttype 23 :consttypmod -1 :constcollid 0 :constlen 4 :constbyval true :constisnull false :location 7 :constvalue 4 [ 1 0 0 0 0 0 0 0 ]} :resno 1 :resname ?column? :ressortgroupref 0 :resorigtbl 0 :resorigcol 0 :resjunk false}) :qual <> :lefttree <> :righttree <> :initPlan <> :extParam (b) :allParam (b) :resconstantqual <>} :rtable ({RTE :alias <> :eref {ALIAS :aliasname *RESULT* :colnames <>} :rtekind 8 :lateral false :inh false :inFromCl false :requiredPerms 0 :checkAsUser 0 :selectedCols (b) :insertedCols (b) :updatedCols (b) :extraUpdatedCols (b) :securityQuals <>}) :resultRelations <> :appendRelations <> :subplans <> :rewindPlanIDs (b) :rowMarks <> :relationOids <> :invalItems <> :paramExecTypes <> :utilityStmt <> :stmt_location 0 :stmt_len 8}
 ?column? 
----------
        1
(1 row)
```