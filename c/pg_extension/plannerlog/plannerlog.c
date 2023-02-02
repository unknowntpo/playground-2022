#include "postgres.h"
#include "fmgr.h"
#include "funcapi.h"
#include "access/reloptions.h"
#include "catalog/pg_type.h"
#include "optimizer/planner.h"
#include "utils/rel.h"

PG_MODULE_MAGIC;

/* The hook function */
static PlannedStmt *
plannerlog_hook(Query *parse, int cursorOptions, ParamListInfo boundParams)
{
    PlannedStmt *stmt;

    /* Call the previous hook */
    stmt = standard_planner(parse, cursorOptions, boundParams);

    /* Log the statement */
    elog(LOG, "Executing statement: %s", nodeToString(stmt));

    return stmt;
}

/* Install the hook */
void
_PG_init(void)
{
    /* Save the previous hook function */
    prev_planner_hook = planner_hook;

    /* Install the hook function */
    planner_hook = plannerlog_hook;
}

/* Uninstall the hook */
void
_PG_fini(void)
{
    /* Restore the previous hook function */
    planner_hook = prev_planner_hook;
}
