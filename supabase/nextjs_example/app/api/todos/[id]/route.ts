import { createClient } from "@/lib/supabase/server";
import { NextRequest, NextResponse } from "next/server";
import { UpdateTodo } from "@/lib/types/database";

/**
 * @swagger
 * /api/todos/{id}:
 *   patch:
 *     description: Update a specific todo
 *     parameters:
 *       - name: id
 *         in: path
 *         required: true
 *         schema:
 *           type: string
 *           format: uuid
 *         description: Todo ID
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/UpdateTodo'
 *     responses:
 *       200:
 *         description: Todo updated successfully
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 data:
 *                   $ref: '#/components/schemas/Todo'
 *       404:
 *         description: Todo not found
 */
export async function PATCH(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params;
    const supabase = await createClient();
    const body: UpdateTodo = await request.json();

    // Validate UUID format
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    if (!uuidRegex.test(id)) {
      return NextResponse.json(
        { error: "Invalid todo ID format" }, 
        { status: 400 }
      );
    }

    // Prepare update data
    const updateData: any = {};
    if (body.title !== undefined) updateData.title = body.title.trim();
    if (body.description !== undefined) updateData.description = body.description?.trim() || null;
    if (body.completed !== undefined) updateData.completed = body.completed;

    // Validate at least one field to update
    if (Object.keys(updateData).length === 0) {
      return NextResponse.json(
        { error: "No fields to update" }, 
        { status: 400 }
      );
    }

    const { data: todo, error } = await supabase
      .from("todos")
      .update(updateData)
      .eq("id", id)
      .select()
      .single();

    if (error) {
      console.error("Error updating todo:", error);
      if (error.code === 'PGRST116') {
        return NextResponse.json(
          { error: "Todo not found" }, 
          { status: 404 }
        );
      }
      return NextResponse.json(
        { error: "Failed to update todo" }, 
        { status: 500 }
      );
    }

    return NextResponse.json({ data: todo });

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
 * /api/todos/{id}:
 *   delete:
 *     description: Delete a specific todo
 *     parameters:
 *       - name: id
 *         in: path
 *         required: true
 *         schema:
 *           type: string
 *           format: uuid
 *         description: Todo ID
 *     responses:
 *       200:
 *         description: Todo deleted successfully
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 message:
 *                   type: string
 *       404:
 *         description: Todo not found
 */
export async function DELETE(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params;
    const supabase = await createClient();

    // Validate UUID format
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    if (!uuidRegex.test(id)) {
      return NextResponse.json(
        { error: "Invalid todo ID format" }, 
        { status: 400 }
      );
    }

    const { error } = await supabase
      .from("todos")
      .delete()
      .eq("id", id);

    if (error) {
      console.error("Error deleting todo:", error);
      if (error.code === 'PGRST116') {
        return NextResponse.json(
          { error: "Todo not found" }, 
          { status: 404 }
        );
      }
      return NextResponse.json(
        { error: "Failed to delete todo" }, 
        { status: 500 }
      );
    }

    return NextResponse.json({ message: "Todo deleted successfully" });

  } catch (error) {
    console.error("API Error:", error);
    return NextResponse.json(
      { error: "Internal server error" }, 
      { status: 500 }
    );
  }
}