import time
from kubernetes import client, config, watch


def main():
    # Load the Kubernetes configuration from default location
    config.load_kube_config()

    # Create an instance of the Kubernetes API client
    v1 = client.CoreV1Api()

    # Initialize a watch on Pod resources
    w = watch.Watch()

    try:
        # Watch for Pod events
        for event in w.stream(v1.list_pod_for_all_namespaces):
            pod = event['object']
            event_type = event['type']
            print(
                f"Event: {event_type}, Pod: {pod.metadata.name}, Namespace: {pod.metadata.namespace}")
    except KeyboardInterrupt:
        pass
    finally:
        w.stop()


if __name__ == '__main__':
    main()
