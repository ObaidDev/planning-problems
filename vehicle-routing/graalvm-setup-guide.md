# Setting Up GraalVM Native Image: A Complete Guide for Linux

## Prerequisites
- Linux operating system
- Basic understanding of command line operations

## Step-by-Step Setup Process

### 1. Setting Up Environment Variables
First, configure your environment variables to point to your GraalVM installation:

```bash
export GRAALVM_HOME=/path/to/your/graalvm
export JAVA_HOME=$GRAALVM_HOME
export PATH=$GRAALVM_HOME/bin:$PATH
```

Make these changes permanent by adding them to your shell configuration:
```bash
echo 'export GRAALVM_HOME=/path/to/your/graalvm' >> ~/.bashrc
echo 'export JAVA_HOME=$GRAALVM_HOME' >> ~/.bashrc
echo 'export PATH=$GRAALVM_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### 2. Installing Required Dependencies
Install essential build tools on your Linux system:
```bash
sudo apt-get update
sudo apt-get install build-essential libz-dev zlib1g-dev
```

For RPM-based distributions (like Red Hat, CentOS, Fedora):
```bash
sudo dnf groupinstall "Development Tools"
sudo dnf install zlib-devel
```

### 3. Installing Native Image Tool
The native-image tool isn't included by default with GraalVM. Install it using GraalVM's updater:
```bash
$GRAALVM_HOME/bin/gu install native-image
```

### 4. Verification Steps
Verify your installation with these commands:
```bash
java --version        # Should show GraalVM version
native-image --version
echo $GRAALVM_HOME   # Should show your GraalVM path
```

### 5. Common Issues and Solutions

#### Permission Issues
If you encounter permission problems with Maven builds:
```bash
sudo chown -R $(whoami):$(whoami) .
```

#### Native Image Not Found
If Maven can't find native-image, ensure:
- GRAALVM_HOME is correctly set
- native-image is installed
- PATH includes GraalVM's bin directory

## Next Steps
With this setup complete, you can now:
- Build native images from your Java applications
- Use native image with frameworks like Spring Boot, Quarkus, or Micronaut
- Enjoy faster startup times and lower memory consumption

## Tips for Best Results
- Always verify environment variables after a new terminal session
- Keep GraalVM and native-image tool updated
- Check framework-specific guides for additional configuration requirements

#Java #GraalVM #NativeImage #Performance #CloudNative #DevOps
