# Scanventory

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpack-compose&logoColor=white)
![License](https://img.shields.io/github/license/BlueDarkUP/Scanventory)

Scanventory 是一个专为国企和类似机构设计的开源固定资产盘点工具。它旨在彻底颠覆传统盘点方式，让您告别昂贵、复杂的专业设备和软件，只需通过智能手机即可高效、便捷地完成资产盘点工作。

## ✨ 核心功能

*   **手机扫码，高效盘点：** 利用手机摄像头，快速扫描资产上的条形码或二维码，即时更新盘点状态。
*   **Excel 表格导入：** 简单上传您的资产清单 Excel 文件（支持 `.xlsx` 格式），软件将自动识别并导入资产数据。
*   **本地数据处理与存储：** 所有盘点数据均在本地设备上安全处理和存储，无需外部服务器，保障数据隐私。
*   **资产状态实时管理：** 清晰区分“在库”和“已盘点”资产，实时掌握盘点进度。
*   **灵活数据导出：** 支持将“未盘点资产列表”和“已盘点资产列表”导出为 Excel 文件，方便数据分析、审计和汇报。
*   **资产详情查看：** 针对单个资产，可查看其完整的详细信息。
*   **搜索与筛选：** 通过资产编码进行搜索，快速定位特定资产。
*   **一键清空数据：** 方便地清除所有本地资产数据，为新的盘点任务做准备。
*   **低成本、高效率：** 作为一个开源解决方案，Scanventory 旨在帮助企业大幅降低固定资产盘点成本，显著提升工作效率。

## 🚀 快速开始

### 开发环境要求

*   Android Studio Arctic Fox (2020.3.1) 或更高版本
*   Android SDK 级别 21 (Android 5.0 Lollipop) 或更高

### 构建与运行

1.  **克隆仓库：**
    ```bash
    git clone https://github.com/BlueDarkUP/Scanventory.git
    ```
2.  **在 Android Studio 中打开项目：**
    打开 Android Studio，选择 `File` -> `Open`，然后导航到您克隆的 `Scanventory` 目录并打开。
3.  **同步 Gradle 项目：**
    等待 Gradle 同步完成，确保所有依赖都已下载。
4.  **连接设备或启动模拟器：**
    连接一台 Android 手机（启用 USB 调试）或启动一个 Android 模拟器。
5.  **运行应用：**
    点击 Android Studio 工具栏上的 `Run` 按钮 (绿色播放图标)，选择您的设备或模拟器，即可安装并运行 Scanventory 应用。

### 使用指南

1.  **导入资产数据：**
    *   首次使用或需要更新资产清单时，点击右上角的 **导入 (⬆️)** 图标。
    *   选择包含资产信息的 Excel 文件 (`.xlsx`)。请确保 Excel 文件包含 "资产编码" 和 "资产名称" 列，建议包含所有 [定义在 `Asset.kt` 中的列](https://github.com/BlueDarkUP/Scanventory/blob/main/app/src/main/java/com/example/bbmg_zebra/MainActivity.kt#L45)。
2.  **开始盘点：**
    *   在主界面，点击底部的 **“开始扫描”** 按钮。
    *   将手机摄像头对准资产上的二维码或条形码。
    *   成功识别后，资产状态将自动更新为“已盘点”，并在屏幕下方短暂提示。
    *   您也可以通过顶部的搜索框手动输入资产编码进行盘点（如果该资产未盘点）。
3.  **查看已盘点/未盘点列表：**
    *   主界面默认显示“未盘点列表”。
    *   点击右上角的 **“查看已盘点列表 (✅)”** 图标，可以查看所有已盘点资产。
    *   在已盘点列表中，您可以撤销某个资产的盘点状态。
4.  **导出盘点结果：**
    *   在主界面（未盘点列表），点击右上角的 **“导出未盘点列表 (⬇️)”** 图标。
    *   在已盘点列表界面，点击右上角的 **“导出已盘点列表 (⬇️)”** 图标。
    *   选择保存位置，即可导出当前的资产清单。
5.  **清空所有数据：**
    *   点击右上角的 **“清除所有数据 (🧹)”** 图标。
    *   确认操作后，所有本地存储的资产数据将被删除。

## 🛠️ 技术栈

*   **语言:** Kotlin
*   **UI 框架:** Jetpack Compose
*   **数据库:** SQLite (通过 Android 的 `SQLiteOpenHelper`)
*   **Excel 处理:** Apache POI
*   **条码扫描:** ZXing Android Embedded

## 🤝 贡献

Scanventory 是一个开源项目，我们欢迎所有形式的贡献！无论是提交 Bug 报告、提出功能建议、改进代码，还是完善文档，您的帮助都将使这个项目变得更好。

1.  **Fork** 本仓库。
2.  创建您的特性分支 (`git checkout -b feature/AmazingFeature`)。
3.  提交您的更改 (`git commit -m 'Add some AmazingFeature'`)。
4.  推送到分支 (`git push origin feature/AmazingFeature`)。
5.  打开一个 **Pull Request**。

## 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。

## 📞 联系方式

如果您有任何问题或建议，欢迎通过 GitHub Issues 提出，或者联系我：

**BlueDarkUP@Gmail.com**
**Blue_DarkMC@Outlook.com**

---
