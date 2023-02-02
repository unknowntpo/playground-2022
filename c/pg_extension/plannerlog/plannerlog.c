#include "postgres.h"
#include "nodes/execnodes.h"
#include "optimizer/planner.h"
#include "optimizer/planmain.h"
#include "utils/elog.h"
#include "executor/spi.h"

static PlannedStmt *
plannerlog_hook(Query *parse, int cursorOptions, ParamListInfo boundParams)
{
    PlannedStmt *result;

    result = standard_planner(parse, cursorOptions, boundParams);

    /* Log the decision made by the query planner here */
    ereport(NOTICE, (errmsg("Query planner decision: %s", decision_str)));

    return result;
}

void _PG_init(void)
{
    planner_hook = plannerlog_hook;
}
