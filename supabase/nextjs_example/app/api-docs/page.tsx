'use client';

import dynamic from 'next/dynamic';

const SwaggerUI = dynamic(() => import('swagger-ui-react'), {
  ssr: false,
});

import 'swagger-ui-react/swagger-ui-bundle.css';

export default function ApiDocsPage() {
  return (
    <div className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-8">API Documentation</h1>
      <SwaggerUI url="/api/docs" />
    </div>
  );
}