---
description: Run Maven tests using the discovered IDEA Maven installation
---

To run tests in this project, use the following PowerShell command which uses the Maven installation bundled with IntelliJ IDEA:

// turbo
1. Run the specific test:
```powershell
& "C:\Software\idea-2025.3.1.win\plugins\maven\lib\maven3\bin\mvn.cmd" test -Dtest=AVLTreeTest
```

2. To run all tests:
```powershell
& "C:\Software\idea-2025.3.1.win\plugins\maven\lib\maven3\bin\mvn.cmd" test
```

> [!NOTE]
> This path was discovered automatically as `mvn` was not in the system PATH.
