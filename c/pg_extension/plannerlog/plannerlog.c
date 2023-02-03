#include "postgres.h"
#include "fmgr.h"
#include "funcapi.h"
#include "access/reloptions.h"
#include "catalog/pg_type.h"
#include "optimizer/planner.h"
#include "utils/rel.h"

PG_MODULE_MAGIC;

/* The hook function */
PlannedStmt *
plannerlog_hook(Query *parse, const char *query_string,
				int cursorOptions,
				ParamListInfo boundParams)
{
	PlannedStmt *stmt;

	/* Call the previous hook */
	// extern PlannedStmt *standard_planner(Query *parse, const char *query_string,
	// 									 int cursorOptions,
	// 									 ParamListInfo boundParams);

	stmt = standard_planner(parse, query_string, cursorOptions, boundParams);

	/* Log the statement */
	errmsg(LOG, "Executing statement: %s", nodeToString(stmt));

	return stmt;
}

PGDLLIMPORT planner_hook_type prev_planner_hook;

void _PG_init(void);

/* Install the hook */
void _PG_init(void)
{
	/* Save the previous hook function */
	prev_planner_hook = planner_hook;

	/* Install the hook function */
	planner_hook = plannerlog_hook;
}

/* Uninstall the hook */
void _PG_fini(void)
{
	/* Restore the previous hook function */
	planner_hook = prev_planner_hook;
}