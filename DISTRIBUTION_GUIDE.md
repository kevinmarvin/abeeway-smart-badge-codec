# Distribution Guide for Abeeway Smart Badge Codec

This guide covers all options for distributing and using the Abeeway Smart Badge Codec library.

## 📦 Package Information

**New Package Name**: `com.github.kevinmarvin.abeeway`  
**Maven Coordinates**: `com.github.kevinmarvin:abeeway-smart-badge-codec:1.0.0`

## 🎯 Distribution Options (Ranked by Ease)

---

## Option 1: JitPack (Recommended - Easiest) 🚀

**Best for**: Open source projects, quick setup, public distribution

### Advantages:
- ✅ **Zero setup required**
- ✅ **Builds directly from GitHub releases**
- ✅ **Free for public repositories**
- ✅ **Automatic versioning from Git tags**
- ✅ **No account registration needed**

### Setup:

1. **Create a GitHub Release**:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   # Or create release through GitHub UI
   ```

2. **Users add JitPack repository to their pom.xml**:
   ```xml
   <repositories>
       <repository>
           <id>jitpack.io</id>
           <url>https://jitpack.io</url>
       </repository>
   </repositories>
   
   <dependencies>
       <dependency>
           <groupId>com.github.kevinmarvin</groupId>
           <artifactId>abeeway-smart-badge-codec</artifactId>
           <version>v1.0.0</version>
       </dependency>
   </dependencies>
   ```

3. **That's it!** JitPack automatically builds and serves your library.

### Usage Example:
```java
import com.github.kevinmarvin.abeeway.AbeewaySmartBadgeCodec;
import com.github.kevinmarvin.abeeway.parameters.AbeewayParams;

AbeewaySmartBadgeCodec codec = new AbeewaySmartBadgeCodec();
AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
    .setGpsTimeout(120)
    .build();
```

---

## Option 2: GitHub Packages 📦

**Best for**: Private/team projects, GitHub integration

### Advantages:
- ✅ **Integrated with GitHub**
- ✅ **Free for public repos**
- ✅ **Good for private/team distribution**
- ✅ **Access control via GitHub permissions**

### Setup:

1. **Configure pom.xml for distribution**:
   ```xml
   <distributionManagement>
       <repository>
           <id>github</id>
           <name>GitHub kevinmarvin Apache Maven Packages</name>
           <url>https://maven.pkg.github.com/kevinmarvin/abeeway-smart-badge-codec</url>
       </repository>
   </distributionManagement>
   ```

2. **Configure GitHub authentication** (~/.m2/settings.xml):
   ```xml
   <servers>
       <server>
           <id>github</id>
           <username>kevinmarvin</username>
           <password>YOUR_GITHUB_TOKEN</password>
       </server>
   </servers>
   ```

3. **Deploy to GitHub Packages**:
   ```bash
   mvn clean deploy
   ```

### Users add to their pom.xml:
```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/kevinmarvin/abeeway-smart-badge-codec</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.kevinmarvin</groupId>
        <artifactId>abeeway-smart-badge-codec</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

---

## Option 3: Local Installation (Simplest for Your Own Projects) 🏠

**Best for**: Your own projects, quick testing

### Setup:
```bash
# In the library directory
mvn clean install

# This installs to your local ~/.m2/repository
```

### Usage in your other projects:
```xml
<dependencies>
    <dependency>
        <groupId>com.github.kevinmarvin</groupId>
        <artifactId>abeeway-smart-badge-codec</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

---

## Option 4: Maven Central (Most Professional) 🏛️

**Best for**: Professional/enterprise distribution, maximum discoverability

### Requirements:
- ✅ **Domain ownership** (for com.github.kevinmarvin you're fine)
- ✅ **Sonatype OSSRH account**
- ✅ **GPG key for signing**
- ✅ **Complete project metadata**

### Setup Process:

1. **Create Sonatype JIRA account**: https://issues.sonatype.org/

2. **Request namespace**: Create ticket for `com.github.kevinmarvin`

3. **Configure pom.xml**:
   ```xml
   <distributionManagement>
       <snapshotRepository>
           <id>ossrh</id>
           <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
       </snapshotRepository>
       <repository>
           <id>ossrh</id>
           <url>https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/</url>
       </repository>
   </distributionManagement>
   ```

4. **Add required plugins** (GPG signing, source/javadoc generation)

5. **Deploy**:
   ```bash
   mvn clean deploy -Prelease
   ```

### Users access via standard Maven:
```xml
<dependencies>
    <dependency>
        <groupId>com.github.kevinmarvin</groupId>
        <artifactId>abeeway-smart-badge-codec</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

---

## 🎯 **Recommendation Matrix**

| Use Case | Recommended Option | Why |
|----------|-------------------|-----|
| **Your own projects** | Local Install | Fastest, zero setup |
| **Open source sharing** | JitPack | Zero maintenance, automatic |
| **Team/company projects** | GitHub Packages | Access control, GitHub integration |
| **Professional distribution** | Maven Central | Maximum discoverability, trusted |

---

## 🚀 **Quick Start for Your Use Case**

Since you want to include this in another project, here's the **fastest path**:

### For Immediate Use (Today):
```bash
# In abeeway-smart-badge-codec directory
mvn clean install

# In your other project's pom.xml
<dependency>
    <groupId>com.github.kevinmarvin</groupId>
    <artifactId>abeeway-smart-badge-codec</artifactId>
    <version>1.0.0</version>
</dependency>
```

### For Sharing/Team Use (Recommended):
1. **Create GitHub Release** (tag v1.0.0)
2. **Use JitPack** - zero setup, automatic building
3. **Users add JitPack repository to pom.xml**

### Example JitPack Integration:
```xml
<!-- Add to your other project's pom.xml -->
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.kevinmarvin</groupId>
        <artifactId>abeeway-smart-badge-codec</artifactId>
        <version>v1.0.0</version>
    </dependency>
</dependencies>
```

---

## 🔧 **Package Name Benefits**

The new package name `com.github.kevinmarvin.abeeway` provides:

✅ **Proper namespace**: Clearly indicates this is your implementation  
✅ **GitHub convention**: Standard pattern for GitHub-hosted libraries  
✅ **No trademark issues**: Avoids using Abeeway's namespace directly  
✅ **Maven compatibility**: Works with all Maven repositories  
✅ **Future-proof**: Easy to migrate between distribution options  

---

## 📝 **Updated Usage Examples**

```java
// New import statements
import com.github.kevinmarvin.abeeway.AbeewaySmartBadgeCodec;
import com.github.kevinmarvin.abeeway.parameters.AbeewayParams;
import com.github.kevinmarvin.abeeway.models.DecodedUplink;

// Usage remains exactly the same!
AbeewaySmartBadgeCodec codec = new AbeewaySmartBadgeCodec();
AbeewayParams params = AbeewaySmartBadgeCodec.newParameters()
    .setGpsTimeout(120)
    .setMotionSensitivity(AbeewayParams.MotionSensitivity.HIGH)
    .build();
```

The package name change is purely organizational - all functionality remains identical!