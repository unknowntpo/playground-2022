# AWS Practice - Complete Learning Path

This is an AWS playground designed for rapid skill acquisition and practical application.

**Background**: Senior Engineer, AWS newbie seeking production-ready knowledge.

## Tech Stack
- **Terraform** - Infrastructure as Code for rapid deployment
- **Python** - Demo applications and automation scripts

## Primary Goal
Learn AWS concepts quickly to build and deploy full-stack applications using Frontend + Backend architecture with AWS services.

**⚠️ COST WARNING**: Always use AWS Free Tier. Don't make me pay money!

---

## 📋 Learning Checklist

### Phase 1: Foundation & Account Setup
- [ ] **AWS Account Setup**
  - [ ] Create AWS Free Tier account
  - [ ] Enable billing alerts ($5, $10, $20 thresholds)
  - [ ] Set up MFA for root account
  - [ ] Create IAM user with appropriate permissions
  - [ ] Install AWS CLI and configure credentials
  - [ ] Test basic AWS CLI commands (`aws sts get-caller-identity`)

- [ ] **Development Environment**
  - [ ] Install Terraform
  - [ ] Set up Python virtual environment
  - [ ] Install boto3 (AWS Python SDK)
  - [ ] Create project structure for infrastructure and application code

### Phase 2: Core Storage & Content Delivery
- [ ] **S3 (Simple Storage Service)**
  - [ ] Create S3 bucket with Terraform
  - [ ] Upload files programmatically with Python
  - [ ] Configure bucket policies and permissions
  - [ ] Enable versioning and lifecycle policies
  - [ ] Set up static website hosting
  - [ ] **Exercise**: Deploy a simple static website

- [ ] **CloudFront (CDN)**
  - [ ] Create CloudFront distribution for S3 bucket
  - [ ] Configure custom domain (optional)
  - [ ] Set up cache behaviors and TTL
  - [ ] **Exercise**: Accelerate static website delivery globally

### Phase 3: Computing & Application Hosting
- [ ] **EC2 (Elastic Compute Cloud)**
  - [ ] Launch EC2 instance with Terraform
  - [ ] Configure security groups and key pairs
  - [ ] Install and configure web server (nginx/Apache)
  - [ ] Deploy Python Flask/FastAPI application
  - [ ] **Exercise**: Build a REST API backend

- [ ] **Lambda (Serverless)**
  - [ ] Create Lambda function with Terraform
  - [ ] Deploy Python function with dependencies
  - [ ] Configure triggers (API Gateway, S3, CloudWatch)
  - [ ] **Exercise**: Build serverless API endpoint

### Phase 4: Database & Data Management
- [ ] **RDS (Relational Database Service)**
  - [ ] Create PostgreSQL/MySQL instance (Free Tier)
  - [ ] Configure security groups and parameter groups
  - [ ] Connect from Python application
  - [ ] **Exercise**: Build CRUD application with database

- [ ] **DynamoDB (NoSQL)**
  - [ ] Create DynamoDB table with Terraform
  - [ ] Design partition key and sort key strategy
  - [ ] Implement CRUD operations in Python
  - [ ] **Exercise**: Build user session management

### Phase 5: Messaging & Event-Driven Architecture
- [ ] **SQS (Simple Queue Service)**
  - [ ] Create standard and FIFO queues
  - [ ] Send/receive messages with Python
  - [ ] Configure dead letter queues
  - [ ] **Exercise**: Build async task processing system

- [ ] **SNS (Simple Notification Service)**
  - [ ] Create topics and subscriptions
  - [ ] Integrate with email, SMS notifications
  - [ ] Connect SNS to Lambda functions
  - [ ] **Exercise**: Build notification system

### Phase 6: API Management & Security
- [ ] **API Gateway**
  - [ ] Create REST API with Terraform
  - [ ] Configure stages (dev, prod)
  - [ ] Set up authentication (API keys, Cognito)
  - [ ] **Exercise**: Build secure API with rate limiting

- [ ] **Cognito (User Management)**
  - [ ] Create user pools and identity pools
  - [ ] Implement signup/signin with Python
  - [ ] Configure MFA and password policies
  - [ ] **Exercise**: Build user authentication system

### Phase 7: Monitoring & DevOps
- [ ] **CloudWatch**
  - [ ] Set up custom metrics and alarms
  - [ ] Create dashboards for application monitoring
  - [ ] Configure log aggregation
  - [ ] **Exercise**: Monitor application performance

- [ ] **VPC (Virtual Private Cloud)**
  - [ ] Design network architecture
  - [ ] Create subnets, route tables, NAT gateways
  - [ ] Configure security groups and NACLs
  - [ ] **Exercise**: Deploy multi-tier application

### Phase 8: Advanced Integration Projects

#### Project 1: Full-Stack Web Application
- [ ] **Frontend**: React app hosted on S3 + CloudFront
- [ ] **Backend**: Lambda functions + API Gateway
- [ ] **Database**: DynamoDB for user data
- [ ] **Auth**: Cognito for user management
- [ ] **Storage**: S3 for file uploads
- [ ] **Monitoring**: CloudWatch logs and metrics

#### Project 2: Microservices Architecture
- [ ] **API Gateway**: Central entry point
- [ ] **Lambda**: Individual microservices
- [ ] **SQS/SNS**: Inter-service communication
- [ ] **RDS**: Shared database layer
- [ ] **VPC**: Network segmentation

#### Project 3: Event-Driven System
- [ ] **S3**: File upload triggers
- [ ] **Lambda**: Image/document processing
- [ ] **SQS**: Task queue management
- [ ] **SNS**: Status notifications
- [ ] **DynamoDB**: Metadata storage

---

## 🏗️ Project Structure
```
aws_practice/
├── terraform/
│   ├── modules/
│   │   ├── s3/
│   │   ├── lambda/
│   │   ├── rds/
│   │   └── vpc/
│   ├── environments/
│   │   ├── dev/
│   │   └── prod/
│   └── main.tf
├── python/
│   ├── lambda_functions/
│   ├── web_apps/
│   ├── scripts/
│   └── requirements.txt
├── docs/
└── examples/
```

## 💰 Cost Management Checklist
- [ ] Set up billing alerts at $5, $10, $20
- [ ] Review AWS Free Tier usage monthly
- [ ] Use AWS Cost Explorer for cost analysis
- [ ] Tag all resources for cost tracking
- [ ] Implement auto-shutdown for non-production resources
- [ ] Use Terraform to destroy unused infrastructure

## 📚 Learning Resources
- [ ] AWS Free Tier documentation
- [ ] AWS Well-Architected Framework
- [ ] Terraform AWS Provider documentation
- [ ] boto3 Python SDK documentation

## 🎯 Success Metrics
- [ ] Deploy production-ready full-stack application
- [ ] Stay within AWS Free Tier limits
- [ ] Implement infrastructure as code for all resources
- [ ] Build CI/CD pipeline for automated deployments
- [ ] Achieve < 2 second page load times with CloudFront

---

**Remember**: Each checkbox represents hands-on practice. Theory without implementation doesn't count!
