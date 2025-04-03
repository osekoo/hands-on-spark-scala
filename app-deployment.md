# Spark Application Deployment Lab on the LAMSADE Cluster

In this lab, you will deploy and run a Spark application on the LAMSADE cluster. You will learn how to transfer data, upload your application, run it with Spark, and monitor progress using the Spark UI.


## Requirements

Before starting, make sure you:
- Know how to use basic command-line tools (Linux/macOS/PowerShell)
- Understand the structure of a Spark/Scala application
- Have your LAMSADE cluster **account key**

## Step 1 – Request Your SSH Key

If you don’t have your account key, ask **Dario**.

Your key file will look like:
```
id_123_<your_username>.key
```

## Step 2 – Set Correct Permissions on the Key File

> SSH will reject your key if it is too visible to others.

### On Linux/macOS
```bash
chmod 600 <your_account_key>
```

### On Windows
- Right-click on the key file → Properties → Security tab
- Remove access for `Users` or `Everyone`
- Make sure **only your user** has Full Control


## Step 3 – Connect to the LAMSADE Cluster

```bash
ssh -p 5022 -i <your_account_key> <your_username>@ssh.lamsade.dauphine.fr
```

- Replace `<your_account_key>` with your file path (e.g., `~/Downloads/id_123.key`)
- Replace `<your_username>` with your login


## Step 4 – Create Your Workspace

On the remote server:
```bash
mkdir -p ~/workspace/data
ls ~/workspace
```

You should see the `data/` folder listed.


## Step 5 – Upload Your Data Files

Choose one method:

### a. From Your Local Machine (SCP)
```bash
scp -P 5022 -i <your_account_key> <your_local_file> <your_username>@ssh.lamsade.dauphine.fr:~/workspace/data
```

### b. From the Internet (Remote Server)
```bash
wget -P ~/workspace/data <file_url>
```

### c. From HDFS (Remote Server)
```bash
hdfs dfs -get /path/to/your/data ~/workspace/data
```


## Step 6 – Upload Files to HDFS

```bash
cd ~/workspace

# Create a directory in HDFS (if needed)
hdfs dfs -mkdir -p data

# Upload all files from local ~/workspace/data to HDFS
hdfs dfs -put data/* data/

# Confirm files are uploaded
hdfs dfs -ls data
```

> In your Spark app, you can now reference files like: `data/my_file.csv`


## Step 7 – Upload Your Spark JAR File

```bash
scp -P 5022 -i <your_account_key> <your_jar_file> <your_username>@ssh.lamsade.dauphine.fr:~/workspace
```

## Step 8 – Run Your Spark Application

```bash
cd ~/workspace

spark-submit --class <main_class> <jar_file> arg1 arg2 ...
```

### Example
```bash
spark-submit --class org.example.MyApp my-spark-app.jar data/input.csv data/output/
```

- `<main_class>`: your application's main class
- `<jar_file>`: the name of your compiled JAR
- `arg1`, `arg2`, etc.: your program's arguments


## Step 9 – Monitor Your Job

### a. Terminal Output
Logs are printed in the terminal.

### b. Spark Web UI (optional but useful)
To access it:
```bash
ssh -p 5022 -i <your_account_key> <your_username>@ssh.lamsade.dauphine.fr -L 8080:vmhadoopmaster.cluster.lamsade.dauphine.fr:8080
```

Then open your browser at:
```
http://localhost:8080
```


## Step 10 – Troubleshooting

| Problem                  | Check                                                  |
|--------------------------|--------------------------------------------------------|
| Permission Denied (SSH)  | Fix key permissions (Step 2)                           |
| File Not Found           | Check if file exists in `~/workspace` or HDFS          |
| Spark Job Fails          | Check logs in terminal or Spark UI                     |
| Cluster Node Issues      | Open YARN UI: http://vmhadoopmaster.cluster.lamsade.dauphine.fr:8088/cluster/nodes |


## Cluster Info

| Component       | Version / Detail                                    |
|------------------|-----------------------------------------------------|
| Spark            | 3.5.1                                               |
| Scala            | 2.12.18                                             |
| Java             | 1.8                                                 |
| Nodes            | 9 nodes (mix of 1G–40G RAM, 2–16 cores)             |
| Spark Master     | `spark://vmhadoopmaster.cluster.lamsade.dauphine.fr:7077` |
| Spark Web UI     | [http://vmhadoopmaster.cluster.lamsade.dauphine.fr:8080](http://vmhadoopmaster.cluster.lamsade.dauphine.fr:8080) |


## Quick Checklist Before Submitting

- [ ] SSH key configured and connection tested  
- [ ] Files uploaded to `~/workspace/data`  
- [ ] Data copied to HDFS (`hdfs dfs -put`)  
- [ ] JAR file uploaded  
- [ ] `spark-submit` command tested  
- [ ] Spark UI accessed (optional)
