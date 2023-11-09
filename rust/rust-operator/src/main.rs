use k8s_openapi::api::core::v1::Pod;
use k8s_openapi::apimachinery::pkg::apis::meta::v1::ObjectMeta;
use k8s_openapi::serde_json;
use kube::{
    api::{ListParams, PostParams, WatchEvent},
    client::Client,
    Api, Error,
};
use log::{error, info};
use log4rs;

#[tokio::main]
async fn main() {
    log4rs::init_file("log4rs.yml", Default::default()).unwrap();
    info!("Hello, world!");
    let client = Client::try_default().await.unwrap();
    let api: Api<Pod> = Api::namespaced(client, "kube-system");
    api.list(&ListParams::default())
        .await
        .unwrap()
        .items
        .iter()
        .map(|pod| pod.metadata.name.clone())
        .for_each(|name| match name {
            Some(name) => info!("{}", name),
            None => error!("error"),
        });
}

// let api: Api<Pod> = Api::namespaced(client, "kube-system");
// api.list(&ListParams::default())
//     .await
//     .unwrap()
//     .items
//     .iter()
//     .map(|pod| pod.name())
//     .for_each(|name| info!("{}", name));
