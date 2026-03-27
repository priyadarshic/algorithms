# Chapter: Understanding Kubernetes

Kubernetes (K8s) is an open-source system for automating deployment, scaling, and management of **containerized applications**. While Docker provides the container, Kubernetes provides the orchestration to manage those containers at scale.

## Why use Kubernetes for GraphsTrees?

- **Service Management**: K8s ensures that the `GraphsTrees` application is always running. If a container crashes, Kubernetes automatically restarts it (Self-healing).
- **Scalability**: You can easily run multiple copies (replicas) of your `HashMapDemo` to handle more tasks if it were a high-traffic service.
- **Resource Management**: Kubernetes intelligently schedules containers on the best available machines (nodes) based on their resource needs.

## Orchestrating the Project

In Kubernetes, we define the desired state of our application using YAML manifests.

### The Deployment (deployment.yaml)

This manifest tells Kubernetes how to run the `GraphsTrees` container.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: graphstrees-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: graphstrees-app
  template:
    metadata:
      labels:
        app: graphstrees-app
    spec:
      containers:
      - name: graphstrees-container
        image: graphstrees:v1
        resources:
          limits:
            memory: "512Mi"
            cpu: "500m"
          requests:
            memory: "256Mi"
            cpu: "250m"
```

## Practical Examples

### 1. Applying the Manifest
Deploy the application to your Kubernetes cluster:
```bash
kubectl apply -f deployment.yaml
```

### 2. Checking the Status
Verify that your pod is running:
```bash
kubectl get pods -l app=graphstrees-app
```

### 3. Viewing Logs
Since `HashMapDemo` is a CLI-based program, you can see its output by reading the pod logs:
```bash
kubectl logs -l app=graphstrees-app
```
*Expected Output*: You will see the results of the `HashMap` investigations directly from the orchestrated container.

## Essential Kubernetes Commands

| Command | Description |
| :--- | :--- |
| `kubectl apply` | Apply a configuration to a resource |
| `kubectl get pods` | List all pods in the cluster |
| `kubectl describe pod` | Show detailed state of a pod |
| `kubectl logs` | Print the logs for a container in a pod |
| `kubectl delete deployment` | Delete the deployment and its pods |
