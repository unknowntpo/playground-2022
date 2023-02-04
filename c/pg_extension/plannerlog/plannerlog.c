#include "postgres.h"
#include "fmgr.h"
#include "funcapi.h"
#include "access/reloptions.h"
#include "catalog/pg_type.h"
#include "optimizer/planner.h"
#include "utils/rel.h"
#include <stdio.h>

PG_MODULE_MAGIC;

PG_FUNCTION_INFO_V1(query_histogram);

PlannedStmt *
plannerlog_hook(Query *parse, const char *query_string,
				int cursorOptions,
				ParamListInfo boundParams);

/* The hook function */
PlannedStmt *
plannerlog_hook(Query *parse, const char *query_string,
				int cursorOptions,
				ParamListInfo boundParams)
{
	PlannedStmt *stmt;

	pg_usleep(1000000L);

	/* Call the previous hook */
	// extern PlannedStmt *standard_planner(Query *parse, const char *query_string,
	// 									 int cursorOptions,
	// 									 ParamListInfo boundParams);

	fprintf(stderr, "hello error: %d\n", 1);

	stmt = standard_planner(parse, query_string, cursorOptions, boundParams);

	/* Log the statement */
	ereport(ERROR, "Executing statement: %s", nodeToString(stmt));

	ereport(FATAL,
			(errcode(ERRCODE_UNIQUE_VIOLATION)));

	errbacktrace();

	return NULL;
	// return stmt;
}

PGDLLIMPORT planner_hook_type prev_planner_hook;

void _PG_init(void);

/* Install the hook */
void _PG_init(void)
{
	/* Install the hook function */
	planner_hook = plannerlog_hook;
}

/* Uninstall the hook */
void _PG_fini(void)
{
	/* Restore the previous hook function */
	planner_hook = NULL;
}