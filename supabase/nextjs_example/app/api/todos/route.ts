import { createClient } from "@/lib/supabase/server";
import { NextRequest, NextResponse } from "next/server";
import { CreateTodo } from "@/lib/types/database";

/**
 * @swagger
 * /api/todos:
 *   get:
 *     description: Get todos with pagination
 *     parameters:
 *       - name: page
 *         in: query
 *         schema:
 *           type: integer
 *           default: 1
 *         description: Page number
 *       - name: limit
 *         in: query
 *         schema:
 *           type: integer
 *           default: 10
 *         description: Items per page
 *     responses:
 *       200:
 *         description: Success
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 data:
 *                   type: array
 *                   items:
 *                     $ref: '#/components/schemas/Todo'
 *                 pagination:
 *                   $ref: '#/components/schemas/Pagination'
 */
export async function GET(request: NextRequest) {
  try {
    const supabase = await createClient();
    const { searchParams } = new URL(request.url);
    
    // Pagination parameters
    const page = parseInt(searchParams.get('page') || '1');
    const limit = parseInt(searchParams.get('limit') || '10');
    const from = (page - 1) * limit;
    const to = from + limit - 1;

    // Fetch todos with pagination and count
    const { data: todos, error, count } = await supabase
      .from("todos")
      .select("*", { count: 'exact' })
      .order("created_at", { ascending: false })
      .range(from, to);

    if (error) {
      console.error("Error fetching todos:", error);
      return NextResponse.json(
        { error: "Failed to fetch todos" }, 
        { status: 500 }
      );
    }

    // Calculate pagination metadata
    const totalPages = Math.ceil((count || 0) / limit);

    return NextResponse.json({
      data: todos,
      pagination: {
        page,
        limit,
        total: count,
        totalPages,
        hasNext: page < totalPages,
        hasPrev: page > 1
      }
    });

  } catch (error) {
    console.error("API Error:", error);
    return NextResponse.json(
      { error: "Internal server error" }, 
      { status: 500 }
    );
  }
}

/**
 * @swagger
 * /api/todos:
 *   post:
 *     description: Create a new todo
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/CreateTodo'
 *     responses:
 *       201:
 *         description: Todo created successfully
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 data:
 *                   $ref: '#/components/schemas/Todo'
 *       400:
 *         description: Bad request
 */
export async function POST(request: NextRequest) {
  try {
    const supabase = await createClient();
    const body: CreateTodo = await request.json();

    // Validate required fields
    if (!body.title?.trim()) {
      return NextResponse.json(
        { error: "Title is required" }, 
        { status: 400 }
      );
    }

    const { data: todo, error } = await supabase
      .from("todos")
      .insert({
        title: body.title.trim(),
        description: body.description?.trim() || null,
        completed: body.completed || false
      })
      .select()
      .single();

    if (error) {
      console.error("Error creating todo:", error);
      return NextResponse.json(
        { error: "Failed to create todo" }, 
        { status: 500 }
      );
    }

    return NextResponse.json({ data: todo }, { status: 201 });

  } catch (error) {
    console.error("API Error:", error);
    return NextResponse.json(
      { error: "Internal server error" }, 
      { status: 500 }
    );
  }
}