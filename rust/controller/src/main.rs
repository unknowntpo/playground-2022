use futures::StreamExt;
use k8s_openapi::api::core::v1::Pod;
use kube::{
    runtime::{controller::Action, watcher::Config, Controller},
    Api, Client, CustomResource,
};
use schemars::JsonSchema;
use serde::{Deserialize, Serialize};
use std::sync::Arc;
use tokio::time::Duration;

// Define your Custom Resource
#[derive(CustomResource, Clone, Debug, Deserialize, Serialize, JsonSchema)]
#[kube(group = "example.com", version = "v1", kind = "MyResource", namespaced)]
pub struct MyResourceSpec {
    pub message: String,
}

// Define the context for your reconciler
#[derive(Clone)]
struct Context {
    client: Client,
}

// Reconcile function
async fn reconcile(resource: Arc<MyResource>, ctx: Arc<Context>) -> Result<Action, kube::Error> {
    let name = resource.metadata.name.as_deref().unwrap_or("unknown");
    let namespace = resource.metadata.namespace.as_deref().unwrap_or("default");

    println!(
        "Reconciling MyResource {} in {} with content {:?}",
        name, namespace, resource.spec
    );

    // Add your reconciliation logic here
    // For example, you might want to create a Pod based on your CRD:
    let _pods: Api<Pod> = Api::namespaced(ctx.client.clone(), namespace);

    // Your pod creation logic would go here

    Ok(Action::requeue(Duration::from_secs(300)))
}

// Error policy
fn error_policy(_resource: Arc<MyResource>, _error: &kube::Error, _ctx: Arc<Context>) -> Action {
    Action::requeue(Duration::from_secs(5))
}

#[tokio::main]
async fn main() -> Result<(), kube::Error> {
    // Initialize the Kubernetes client
    let client = Client::try_default().await?;

    // Create the context
    let context = Arc::new(Context {
        client: client.clone(),
    });

    // Create our CRD API
    let myresources = Api::<MyResource>::all(client);

    // Create and run the controller
    Controller::new(myresources, Config::default())
        .run(reconcile, error_policy, context)
        .for_each(|_| futures::future::ready(()))
        .await;

    Ok(())
}
