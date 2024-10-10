### **Procedure to Run a Spark Application on the LAMSADE Cluster**

#### **1. Request Your Account Key**
Before starting, ensure you have your LAMSADE cluster account key. If you do not have one, contact `Dario` to request your account key.

#### **2. Connect to the Remote Server**
Use SSH to connect to the remote server:
```bash
ssh -i <your_account_key> <user_name>@ssh.lamsade.dauphine.fr -p 5022
```
- **Note:** After running this command, you may be prompted for your password. Ensure your SSH key is correctly set up.

#### **3. Setup Workspace on the Remote Server**
Once connected to the server:
1. **Create a workspace directory** (if it doesn’t already exist) for your Spark project:
   ```bash
   mkdir -p ~/workspace/data
   ```
   This command creates the `workspace` directory and a subdirectory `data` for storing your files.
   
2. **Verify the Directory:**
   - To confirm the workspace is properly created, you can list your directories:
   ```bash
   ls ~/workspace
   ```

#### **4. Transfer Your Data to the Remote Server**
There are several methods to transfer your data files to the `workspace/data` directory:

1. **From your local machine using SCP:**
   ```bash
   scp -P 5022 -i <your_account_key> <your_local_file> <user_name>@ssh.lamsade.dauphine.fr:~/workspace/data
   ```
   This command securely copies your local file to the remote server’s `data` folder.

2. **Download data directly from the internet:**
   ```bash
   wget -P ~/workspace/data <file_url>
   ```
   This downloads the file from the specified URL directly into your `workspace/data` folder.

3. **Copy data from HDFS:**
   If your data is stored in HDFS, you can copy it to the server using:
   ```bash
   hdfs dfs -get /path/to/your/data ~/workspace/data
   ```

#### **5. Transfer the JAR File of Your Application**
Copy your Spark application’s JAR file to the `workspace` directory on the remote server:
```bash
scp -P 5022 -i <your_account_key> <jar_location> <user_name>@ssh.lamsade.dauphine.fr:~/workspace
```
- **Note:** Ensure that your JAR file is correctly compiled and located in the right directory before transfer.

#### **6. Create a Spark Runner Script**
On your local machine, create a script called `spark-run.sh` to automate the Spark job submission. This script should include the `spark-submit` command and any relevant options, for example:

```bash
#!/bin/bash
spark-submit \
    --deploy-mode "client" \
    --master "spark://vmhadoopmaster.cluster.lamsade.dauphine.fr:7077" \
    --executor-cores "4" \
    --executor-memory "2G" \
    --num-executors "2" \
    --packages "" \
    --files "data/sample_input.csv" \  # Add your input data file paths here
    --class "com.example.YourMainClass" \
    "your-spark-app.jar" \  # Path to your JAR file
    --input "data/sample_input.csv" \  # Example input argument
    --output "output/result"  # Example output argument
```

- **Important:** Modify the `--master` option to match your deployment environment (`yarn`, `mesos`, `local[*]`, etc.).
- Add any input/output file arguments specific to your Spark job.

#### **7. Transfer the Runner Script to the Remote Server**
Once the script is created, copy it to the `workspace` directory on the remote server:
```bash
scp -P 5022 -i <your_account_key> <runner_location> <user_name>@ssh.lamsade.dauphine.fr:~/workspace
```

#### **8. Set the Execution Permission**
On the remote server, change the file execution permissions for your script:
```bash
chmod +x ~/workspace/spark-run.sh
```

#### **9. Pre-Execution Checklist**
Before running the script, ensure the following:
1. **All required data files** are available on the remote server and correctly referenced in the `spark-submit` command.
2. **Data files are specified** with the `--files` option in `spark-submit`. You can also use wildcards:
   - Example: `--files "data/*.csv"` will match all CSV files in the `data` directory.
3. **All required Spark packages** are referenced via the `--packages` option.
4. **Correct class name** is provided in the `--class` option.
5. **Correct JAR file** is referenced at the end of the `spark-submit` command.
6. **Deploy mode**: Use `--deploy-mode client` for monitoring job progress, or `--deploy-mode cluster` for background execution.

#### **10. Run the Application**
Now you are ready to execute your Spark job:
```bash
./workspace/spark-run.sh
```

#### **11. Monitor Job Progress and Output**
After running the script, you can monitor the Spark job’s progress and output in a few ways:
1. **Check the output directly** in your terminal.
2. **Access Spark logs**:
   ```bash
   tail -f logs/spark-job.log
   ```
   This will allow you to view live logs from your job. The logs might be available in `/opt/shared/spark-3.5.1-bin-hadoop3/logs/` folder
3. **Use the Spark UI**: If available, access the Spark Web UI for a detailed view of job stages, tasks, and logs at `<cluster_UI_URL>`.
	1. Default Spark UI URL: see the below section `Cluster Access and Management` for further details

#### **12. Troubleshooting**
If any issues arise:
- **Job Failure Logs**: Check the logs for error messages and debugging information using the `tail` command above.
- **Classpath/Package Issues**: Ensure all required dependencies are correctly listed in `--packages` and `--jars` options of `spark-submit`.
- **Data Availability**: Double-check file paths and ensure they are correctly referenced in the script.

---

### Cluster Configuration Overview


#### **Software and Version Matrix**
- **Spark Version**: 3.5.1
- **Scala Version**: 2.12.18
- **Java Version**: 1.8

These are common versions used in Spark environments. It's recommended to ensure that all nodes are running the same versions to avoid compatibility issues.


#### **Cluster Hardware**
- **Number of Nodes**: 9
- **Hardware**: 4x (32G, 16 cores), 1x (40G, 16 cores), 2x (2G, 2 cores), 1x (1G, 2 cores) 

This configuration provides a solid foundation for Spark jobs with moderate to large memory requirements. Ensure that the cluster's workload matches this resource allocation.


#### **Cluster Access and Management**
1. **Spark Master URL**:  
   The Spark master URL is essential for submitting Spark jobs:
   ```
   spark://vmhadoopmaster.cluster.lamsade.dauphine.fr:7077
   ```

2. **Data Nodes Health Overview**:  
   To check the status of data nodes:
   ```
   http://vmhadoopmaster.cluster.lamsade.dauphine.fr:50070/dfshealth.html#tab-datanode
   ```

3. **Spark Web UI**:  
   The Spark Web UI provides insights into running Spark jobs, including stages, tasks, and executors:
   ```
   http://vmhadoopmaster.cluster.lamsade.dauphine.fr:8080/
   ```

4. **YARN Data Nodes UI**:  
   For tracking resource usage on YARN and checking the health of data nodes:
   ```
   http://vmhadoopmaster.cluster.lamsade.dauphine.fr:8088/cluster/nodes
   ```


#### **SSH Tunnel and Port Forwarding**
To access the Spark Web UI and other interfaces securely, you need to create an SSH tunnel with port forwarding. 
This forwards a port on your local machine to the remote server, allowing you to access the UI in your browser as if it were running locally.

##### Example Command:
```bash
ssh -L 8080:vmhadoopmaster.cluster.lamsade.dauphine.fr:8080 -i <account_key> <your_account>@ssh.lamsade.dauphine.fr -p 5022
```

- **Explanation**:  
   - `-L 8080:vmhadoopmaster.cluster.lamsade.dauphine.fr:8080` forwards local port 8080 to the remote server's port 8080.
   - `<your_account>@ssh.lamsade.dauphine.fr -p 5022` connects you to the SSH gateway.
   
After running this command, you can access the Spark Web UI by browsing to `http://localhost:8080` from your local machine.
