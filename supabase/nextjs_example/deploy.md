# Deployment Guide

## Using Supabase + Vercel (Recommended)

### 1. Create Production Supabase Project
```bash
# Login to Supabase (interactive)
supabase login

# Create new project
supabase projects create todo-app-prod

# Link local to remote
supabase link --project-ref <your-project-ref>

# Push database schema
supabase db push
```

### 2. Deploy to Vercel
```bash
# Install Vercel CLI
npm i -g vercel

# Deploy
vercel

# Set environment variables in Vercel dashboard
# NEXT_PUBLIC_SUPABASE_URL=https://your-project-ref.supabase.co
# NEXT_PUBLIC_SUPABASE_PUBLISHABLE_OR_ANON_KEY=your-anon-key
```

### 3. Alternative: Deploy using GitHub + Vercel
1. Push code to GitHub
2. Connect Vercel to your GitHub repo
3. Set environment variables in Vercel dashboard
4. Automatic deployments on every push

## Using Netlify
```bash
# Install Netlify CLI
npm i -g netlify-cli

# Build and deploy
npm run build
netlify deploy --prod --dir=.next
```

## Environment Variables Needed
- `NEXT_PUBLIC_SUPABASE_URL`
- `NEXT_PUBLIC_SUPABASE_PUBLISHABLE_OR_ANON_KEY`