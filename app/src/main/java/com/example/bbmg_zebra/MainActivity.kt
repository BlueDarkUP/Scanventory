package com.example.bbmg_zebra

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bbmg_zebra.ui.theme.BBMGZEBRATheme
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream
import java.io.InputStream

data class Asset(
    val assetCode: String,
    val assetName: String,
    val location: String?,
    val status: String,
    val assetCategory: String?,
    val department: String?,
    val cardNo: String?,
    val specification: String?,
    val model: String?,
    val quantity: String?,
    val originalValue: String?,
    val accumulatedDepreciation: String?,
    val currentYearDepreciation: String?,
    val netValue: String?,
    val impairmentProvision: String?,
    val netAmount: String?,
    val usingDepartment: String?,
    val user: String?,
    val startDate: String?,
    val usageMonths: String?,
    val initialDepreciation: String?,
    val monthlyDepreciation: String?,
    val netSalvageValue: String?,
    val companyName: String?,
    val inputTax: String?,
    val netSalvageRate: String?
)

object AssetContract {
    object AssetEntry {
        const val TABLE_NAME = "assets"
        const val COLUMN_NAME_ASSET_CODE = "asset_code"
        const val COLUMN_NAME_ASSET_NAME = "asset_name"
        const val COLUMN_NAME_LOCATION = "location"
        const val COLUMN_NAME_STATUS = "status"
        const val COLUMN_NAME_ASSET_CATEGORY = "asset_category"
        const val COLUMN_NAME_DEPARTMENT = "department"
        const val COLUMN_NAME_CARD_NO = "card_no"
        const val COLUMN_NAME_SPECIFICATION = "specification"
        const val COLUMN_NAME_MODEL = "model"
        const val COLUMN_NAME_QUANTITY = "quantity"
        const val COLUMN_NAME_ORIGINAL_VALUE = "original_value"
        const val COLUMN_NAME_ACCUMULATED_DEPRECIATION = "accumulated_depreciation"
        const val COLUMN_NAME_CURRENT_YEAR_DEPRECIATION = "current_year_depreciation"
        const val COLUMN_NAME_NET_VALUE = "net_value"
        const val COLUMN_NAME_IMPAIRMENT_PROVISION = "impairment_provision"
        const val COLUMN_NAME_NET_AMOUNT = "net_amount"
        const val COLUMN_NAME_USING_DEPARTMENT = "using_department"
        const val COLUMN_NAME_USER = "user"
        const val COLUMN_NAME_START_DATE = "start_date"
        const val COLUMN_NAME_USAGE_MONTHS = "usage_months"
        const val COLUMN_NAME_INITIAL_DEPRECIATION = "initial_depreciation"
        const val COLUMN_NAME_MONTHLY_DEPRECIATION = "monthly_depreciation"
        const val COLUMN_NAME_NET_SALVAGE_VALUE = "net_salvage_value"
        const val COLUMN_NAME_COMPANY_NAME = "company_name"
        const val COLUMN_NAME_INPUT_TAX = "input_tax"
        const val COLUMN_NAME_NET_SALVAGE_RATE = "net_salvage_rate"
    }
}

class AssetDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "Assets.db"
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${AssetContract.AssetEntry.TABLE_NAME} (" +
                    "${AssetContract.AssetEntry.COLUMN_NAME_ASSET_CODE} TEXT PRIMARY KEY," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_ASSET_NAME} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_LOCATION} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_STATUS} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_ASSET_CATEGORY} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_DEPARTMENT} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_CARD_NO} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_SPECIFICATION} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_MODEL} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_QUANTITY} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_ORIGINAL_VALUE} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_ACCUMULATED_DEPRECIATION} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_CURRENT_YEAR_DEPRECIATION} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_NET_VALUE} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_IMPAIRMENT_PROVISION} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_NET_AMOUNT} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_USING_DEPARTMENT} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_USER} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_START_DATE} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_USAGE_MONTHS} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_INITIAL_DEPRECIATION} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_MONTHLY_DEPRECIATION} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_NET_SALVAGE_VALUE} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_COMPANY_NAME} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_INPUT_TAX} TEXT," +
                    "${AssetContract.AssetEntry.COLUMN_NAME_NET_SALVAGE_RATE} TEXT)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${AssetContract.AssetEntry.TABLE_NAME}"
    }
    override fun onCreate(db: SQLiteDatabase) { db.execSQL(SQL_CREATE_ENTRIES) }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}

class AssetLocalDatasource(context: Context) {
    private val dbHelper = AssetDbHelper(context)

    fun replaceAllAssets(assets: List<Asset>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete(AssetContract.AssetEntry.TABLE_NAME, null, null)
            assets.forEach { asset ->
                val values = ContentValues().apply {
                    put(AssetContract.AssetEntry.COLUMN_NAME_ASSET_CODE, asset.assetCode)
                    put(AssetContract.AssetEntry.COLUMN_NAME_ASSET_NAME, asset.assetName)
                    put(AssetContract.AssetEntry.COLUMN_NAME_LOCATION, asset.location)
                    put(AssetContract.AssetEntry.COLUMN_NAME_STATUS, "在库")
                    put(AssetContract.AssetEntry.COLUMN_NAME_ASSET_CATEGORY, asset.assetCategory)
                    put(AssetContract.AssetEntry.COLUMN_NAME_DEPARTMENT, asset.department)
                    put(AssetContract.AssetEntry.COLUMN_NAME_CARD_NO, asset.cardNo)
                    put(AssetContract.AssetEntry.COLUMN_NAME_SPECIFICATION, asset.specification)
                    put(AssetContract.AssetEntry.COLUMN_NAME_MODEL, asset.model)
                    put(AssetContract.AssetEntry.COLUMN_NAME_QUANTITY, asset.quantity)
                    put(AssetContract.AssetEntry.COLUMN_NAME_ORIGINAL_VALUE, asset.originalValue)
                    put(AssetContract.AssetEntry.COLUMN_NAME_ACCUMULATED_DEPRECIATION, asset.accumulatedDepreciation)
                    put(AssetContract.AssetEntry.COLUMN_NAME_CURRENT_YEAR_DEPRECIATION, asset.currentYearDepreciation)
                    put(AssetContract.AssetEntry.COLUMN_NAME_NET_VALUE, asset.netValue)
                    put(AssetContract.AssetEntry.COLUMN_NAME_IMPAIRMENT_PROVISION, asset.impairmentProvision)
                    put(AssetContract.AssetEntry.COLUMN_NAME_NET_AMOUNT, asset.netAmount)
                    put(AssetContract.AssetEntry.COLUMN_NAME_USING_DEPARTMENT, asset.usingDepartment)
                    put(AssetContract.AssetEntry.COLUMN_NAME_USER, asset.user)
                    put(AssetContract.AssetEntry.COLUMN_NAME_START_DATE, asset.startDate)
                    put(AssetContract.AssetEntry.COLUMN_NAME_USAGE_MONTHS, asset.usageMonths)
                    put(AssetContract.AssetEntry.COLUMN_NAME_INITIAL_DEPRECIATION, asset.initialDepreciation)
                    put(AssetContract.AssetEntry.COLUMN_NAME_MONTHLY_DEPRECIATION, asset.monthlyDepreciation)
                    put(AssetContract.AssetEntry.COLUMN_NAME_NET_SALVAGE_VALUE, asset.netSalvageValue)
                    put(AssetContract.AssetEntry.COLUMN_NAME_COMPANY_NAME, asset.companyName)
                    put(AssetContract.AssetEntry.COLUMN_NAME_INPUT_TAX, asset.inputTax)
                    put(AssetContract.AssetEntry.COLUMN_NAME_NET_SALVAGE_RATE, asset.netSalvageRate)
                }
                db.insert(AssetContract.AssetEntry.TABLE_NAME, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    private fun cursorToAsset(cursor: Cursor): Asset {
        return Asset(
            assetCode = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_ASSET_CODE)),
            assetName = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_ASSET_NAME)),
            location = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_LOCATION)),
            status = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_STATUS)),
            assetCategory = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_ASSET_CATEGORY)),
            department = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_DEPARTMENT)),
            cardNo = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_CARD_NO)),
            specification = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_SPECIFICATION)),
            model = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_MODEL)),
            quantity = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_QUANTITY)),
            originalValue = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_ORIGINAL_VALUE)),
            accumulatedDepreciation = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_ACCUMULATED_DEPRECIATION)),
            currentYearDepreciation = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_CURRENT_YEAR_DEPRECIATION)),
            netValue = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_NET_VALUE)),
            impairmentProvision = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_IMPAIRMENT_PROVISION)),
            netAmount = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_NET_AMOUNT)),
            usingDepartment = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_USING_DEPARTMENT)),
            user = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_USER)),
            startDate = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_START_DATE)),
            usageMonths = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_USAGE_MONTHS)),
            initialDepreciation = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_INITIAL_DEPRECIATION)),
            monthlyDepreciation = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_MONTHLY_DEPRECIATION)),
            netSalvageValue = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_NET_SALVAGE_VALUE)),
            companyName = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_COMPANY_NAME)),
            inputTax = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_INPUT_TAX)),
            netSalvageRate = cursor.getString(cursor.getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_NET_SALVAGE_RATE))
        )
    }

    fun getUnscannedAssets(): List<Asset> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(AssetContract.AssetEntry.TABLE_NAME, null, "${AssetContract.AssetEntry.COLUMN_NAME_STATUS} != ?", arrayOf("已盘点"), null, null, null)
        val assets = mutableListOf<Asset>()
        while (cursor.moveToNext()) {
            assets.add(cursorToAsset(cursor))
        }
        cursor.close()
        db.close()
        return assets
    }

    fun getScannedAssets(): List<Asset> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(AssetContract.AssetEntry.TABLE_NAME, null, "${AssetContract.AssetEntry.COLUMN_NAME_STATUS} = ?", arrayOf("已盘点"), null, null, null)
        val assets = mutableListOf<Asset>()
        while (cursor.moveToNext()) {
            assets.add(cursorToAsset(cursor))
        }
        cursor.close()
        db.close()
        return assets
    }

    fun getAssetByCode(assetCode: String): Asset? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(AssetContract.AssetEntry.TABLE_NAME, null, "${AssetContract.AssetEntry.COLUMN_NAME_ASSET_CODE} = ?", arrayOf(assetCode), null, null, null, "1")
        var asset: Asset? = null
        if (cursor.moveToFirst()) {
            asset = cursorToAsset(cursor)
        }
        cursor.close()
        db.close()
        return asset
    }

    fun getAllAssetCodes(): List<String> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(AssetContract.AssetEntry.TABLE_NAME, arrayOf(AssetContract.AssetEntry.COLUMN_NAME_ASSET_CODE), null, null, null, null, null)
        val codes = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                codes.add(getString(getColumnIndexOrThrow(AssetContract.AssetEntry.COLUMN_NAME_ASSET_CODE)))
            }
        }
        cursor.close()
        db.close()
        return codes
    }

    fun markAssetAsScanned(assetCode: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AssetContract.AssetEntry.COLUMN_NAME_STATUS, "已盘点")
        }
        val affectedRows = db.update(AssetContract.AssetEntry.TABLE_NAME, values, "${AssetContract.AssetEntry.COLUMN_NAME_ASSET_CODE} = ?", arrayOf(assetCode))
        db.close()
        return affectedRows
    }

    fun unmarkAssetAsScanned(assetCode: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AssetContract.AssetEntry.COLUMN_NAME_STATUS, "在库")
        }
        val affectedRows = db.update(AssetContract.AssetEntry.TABLE_NAME, values, "${AssetContract.AssetEntry.COLUMN_NAME_ASSET_CODE} = ?", arrayOf(assetCode))
        db.close()
        return affectedRows
    }

    fun clearAllAssets() {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete(AssetContract.AssetEntry.TABLE_NAME, null, null)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }
}

object ExcelExporter {
    private val ALL_COLUMNS = listOf(
        "卡片编号", "资产编码", "资产名称", "规格", "型号", "数量", "本币原值",
        "累计折旧", "本年折旧", "净值", "减值准备", "净额", "资产类别", "管理部门",
        "使用部门", "使用人", "存放地点", "开始使用日期", "使用月限", "已计提期初",
        "月折旧额", "净残值", "公司名称", "进项税", "净残值率", "状态"
    )

    fun createExcelFile(assets: List<Asset>): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("资产列表")

        val headerRow = sheet.createRow(0)
        ALL_COLUMNS.forEachIndexed { index, header ->
            headerRow.createCell(index).setCellValue(header)
        }

        assets.forEachIndexed { index, asset ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(asset.cardNo)
            row.createCell(1).setCellValue(asset.assetCode)
            row.createCell(2).setCellValue(asset.assetName)
            row.createCell(3).setCellValue(asset.specification)
            row.createCell(4).setCellValue(asset.model)
            row.createCell(5).setCellValue(asset.quantity)
            row.createCell(6).setCellValue(asset.originalValue)
            row.createCell(7).setCellValue(asset.accumulatedDepreciation)
            row.createCell(8).setCellValue(asset.currentYearDepreciation)
            row.createCell(9).setCellValue(asset.netValue)
            row.createCell(10).setCellValue(asset.impairmentProvision)
            row.createCell(11).setCellValue(asset.netAmount)
            row.createCell(12).setCellValue(asset.assetCategory)
            row.createCell(13).setCellValue(asset.department)
            row.createCell(14).setCellValue(asset.usingDepartment)
            row.createCell(15).setCellValue(asset.user)
            row.createCell(16).setCellValue(asset.location)
            row.createCell(17).setCellValue(asset.startDate)
            row.createCell(18).setCellValue(asset.usageMonths)
            row.createCell(19).setCellValue(asset.initialDepreciation)
            row.createCell(20).setCellValue(asset.monthlyDepreciation)
            row.createCell(21).setCellValue(asset.netSalvageValue)
            row.createCell(22).setCellValue(asset.companyName)
            row.createCell(23).setCellValue(asset.inputTax)
            row.createCell(24).setCellValue(asset.netSalvageRate)
            row.createCell(25).setCellValue(asset.status)
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        workbook.write(byteArrayOutputStream)
        workbook.close()

        return byteArrayOutputStream.toByteArray()
    }
}

object ExcelParser {
    private val ALL_COLUMNS = listOf(
        "卡片编号", "资产编码", "资产名称", "规格", "型号", "数量", "本币原值",
        "累计折旧", "本年折旧", "净值", "减值准备", "净额", "资产类别", "管理部门",
        "使用部门", "使用人", "存放地点", "开始使用日期", "使用月限", "已计提期初",
        "月折旧额", "净残值", "公司名称", "进项税", "净残值率"
    )

    fun parseExcelFiles(context: Context, uris: List<Uri>): Result<List<Asset>> {
        val allAssets = mutableListOf<Asset>()
        try {
            for (uri in uris) {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    allAssets.addAll(parseSingleStream(inputStream))
                }
            }
            val distinctAssets = allAssets.distinctBy { it.assetCode }
            return Result.success(distinctAssets)
        } catch (e: Exception) {
            Log.e("ExcelParser", "Failed to parse Excel file", e)
            return Result.failure(e)
        }
    }

    private fun parseSingleStream(inputStream: InputStream): List<Asset> {
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)
        val headerRow = sheet.getRow(0) ?: return emptyList()

        val columnMap = mutableMapOf<String, Int>()
        headerRow.forEach { cell ->
            ALL_COLUMNS.find { it.equals(cell.stringCellValue, ignoreCase = true) }?.let { colName ->
                columnMap[colName] = cell.columnIndex
            }
        }

        if (!columnMap.containsKey("资产编码") || !columnMap.containsKey("资产名称")) {
            throw Exception("文件中缺少'资产编码'或'资产名称'列。")
        }

        val assets = mutableListOf<Asset>()
        for (i in 1..sheet.lastRowNum) {
            val row = sheet.getRow(i) ?: continue

            val getCellAsString = { colName: String ->
                val colIndex = columnMap[colName]
                if (colIndex != null) {
                    val cell = row.getCell(colIndex)
                    cell?.let {
                        when (it.cellType) {
                            CellType.STRING -> it.stringCellValue.trim()
                            CellType.NUMERIC -> it.numericCellValue.toLong().toString()
                            else -> it.toString().trim()
                        }
                    }
                } else null
            }

            val assetCode = getCellAsString("资产编码") ?: ""
            if (assetCode.isBlank()) continue

            assets.add(
                Asset(
                    assetCode = assetCode,
                    assetName = getCellAsString("资产名称") ?: "N/A",
                    location = getCellAsString("存放地点"),
                    status = "在库",
                    assetCategory = getCellAsString("资产类别"),
                    department = getCellAsString("管理部门"),
                    cardNo = getCellAsString("卡片编号"),
                    specification = getCellAsString("规格"),
                    model = getCellAsString("型号"),
                    quantity = getCellAsString("数量"),
                    originalValue = getCellAsString("本币原值"),
                    accumulatedDepreciation = getCellAsString("累计折旧"),
                    currentYearDepreciation = getCellAsString("本年折旧"),
                    netValue = getCellAsString("净值"),
                    impairmentProvision = getCellAsString("减值准备"),
                    netAmount = getCellAsString("净额"),
                    usingDepartment = getCellAsString("使用部门"),
                    user = getCellAsString("使用人"),
                    startDate = getCellAsString("开始使用日期"),
                    usageMonths = getCellAsString("使用月限"),
                    initialDepreciation = getCellAsString("已计提期初"),
                    monthlyDepreciation = getCellAsString("月折旧额"),
                    netSalvageValue = getCellAsString("净残值"),
                    companyName = getCellAsString("公司名称"),
                    inputTax = getCellAsString("进项税"),
                    netSalvageRate = getCellAsString("净残值率")
                )
            )
        }
        return assets
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BBMGZEBRATheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigator()
                }
            }
        }
    }
}

object Routes {
    const val SPLASH = "splash"
    const val ASSET_LIST = "asset_list"
    const val SCANNED_LIST = "scanned_list"
    const val ASSET_DETAIL = "asset_detail/{assetCode}"
    fun assetDetail(assetCode: String) = "asset_detail/$assetCode"
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(key1 = true) {
        delay(1000L)
        navController.navigate(Routes.ASSET_LIST) {
            popUpTo(Routes.SPLASH) {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Powered By BlueDarkUP",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val localDatasource = AssetLocalDatasource(LocalContext.current)

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(navController = navController)
        }
        composable(Routes.ASSET_LIST) {
            AssetTrackingScreen(navController = navController, localDatasource = localDatasource)
        }
        composable(Routes.SCANNED_LIST) {
            ScannedListScreen(navController = navController, localDatasource = localDatasource)
        }
        composable(Routes.ASSET_DETAIL) { backStackEntry ->
            val assetCode = backStackEntry.arguments?.getString("assetCode")
            if (assetCode != null) {
                AssetDetailScreen(navController = navController, assetCode = assetCode, localDatasource = localDatasource)
            }
        }
    }
}

enum class DialogType { NONE, CLEAR_CACHE }

@Composable
fun ConfirmationDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = text) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) { Text("确认") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannedListScreen(navController: NavHostController, localDatasource: AssetLocalDatasource) {
    var scannedList by remember { mutableStateOf<List<Asset>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val fileSaverLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        onResult = { uri: Uri? ->
            uri?.let {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val excelData = ExcelExporter.createExcelFile(scannedList)
                        context.contentResolver.openOutputStream(it)?.use { outputStream ->
                            outputStream.write(excelData)
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "已盘点列表已导出", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("ExportError", "Failed to export scanned list", e)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "导出失败: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    )

    val loadScannedAssets = {
        coroutineScope.launch {
            isLoading = true
            scannedList = withContext(Dispatchers.IO) { localDatasource.getScannedAssets() }
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadScannedAssets()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("已盘点列表 (${scannedList.size})") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { fileSaverLauncher.launch("已盘点资产.xlsx") },
                        enabled = scannedList.isNotEmpty()
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = "导出已盘点列表")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (scannedList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("暂无已盘点资产", fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(scannedList, key = { it.assetCode }) { asset ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { navController.navigate(Routes.assetDetail(asset.assetCode)) }
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 12.dp, bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(asset.assetName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("编码: ${asset.assetCode}", fontSize = 14.sp)
                                Text("位置: ${asset.location ?: "N/A"}", fontSize = 14.sp, color = Color.Gray)
                            }
                            TextButton(
                                onClick = {
                                    coroutineScope.launch {
                                        withContext(Dispatchers.IO) { localDatasource.unmarkAssetAsScanned(asset.assetCode) }
                                        Toast.makeText(context, "${asset.assetName} 已撤销盘点", Toast.LENGTH_SHORT).show()
                                        loadScannedAssets()
                                    }
                                },
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text("撤销", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ScannerWithFlashlight(
    onBarcodeScanned: (String) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val barcodeView = remember {
        DecoratedBarcodeView(context).apply {
            this.setStatusText("请将二维码/条形码对准扫描框")
            this.decodeContinuous { result ->
                result.text?.let { barcode ->
                    if (barcode.isNotBlank()) {
                        // 暂停扫描以避免多次回调
                        this.pause()
                        onBarcodeScanned(barcode)
                    }
                }
            }
        }
    }
    var isTorchOn by remember { mutableStateOf(false) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
            if (!granted) {
                Toast.makeText(context, "需要相机权限才能扫描", Toast.LENGTH_LONG).show()
                onClose()
            } else {
                // 权限被授予后，立即尝试启动预览
                barcodeView.resume()
            }
        }
    )

    // 这个 effect 确保在 Composable 进入屏幕时请求权限
    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // 这个 effect 管理相机的暂停和恢复
    DisposableEffect(lifecycleOwner, hasCameraPermission) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    // 只有在拥有权限时才恢复
                    if (hasCameraPermission) {
                        barcodeView.resume()
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    barcodeView.pause()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // 确保在视图销毁时停止相机
            barcodeView.pause()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { barcodeView }
            )
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Close, contentDescription = "关闭", tint = Color.White, modifier = Modifier.size(32.dp))
        }

        // 只有在设备支持闪光灯时才显示按钮
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            IconButton(
                onClick = {
                    isTorchOn = !isTorchOn
                    if (isTorchOn) barcodeView.setTorchOn() else barcodeView.setTorchOff()
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                val tint = if (isTorchOn) Color.Yellow else Color.White
                Icon(Icons.Filled.FlashlightOn, contentDescription = "手电筒", tint = tint, modifier = Modifier.size(32.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetTrackingScreen(navController: NavHostController, localDatasource: AssetLocalDatasource) {
    var unscannedAssetList by remember { mutableStateOf<List<Asset>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("请导入资产Excel文件") }
    var dialogToShow by remember { mutableStateOf(DialogType.NONE) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var assetBeingProcessed by remember { mutableStateOf<String?>(null) }
    var showScannerDialog by remember { mutableStateOf(false) }

    val fileExporterLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        onResult = { uri: Uri? ->
            uri?.let {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val excelData = ExcelExporter.createExcelFile(unscannedAssetList)
                        context.contentResolver.openOutputStream(it)?.use { outputStream ->
                            outputStream.write(excelData)
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "未盘点列表已导出", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("ExportError", "Failed to export unscanned list", e)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "导出失败: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    )

    val loadFromLocalDb = {
        coroutineScope.launch(Dispatchers.IO) {
            isLoading = true
            unscannedAssetList = localDatasource.getUnscannedAssets()
            withContext(Dispatchers.Main) {
                if (unscannedAssetList.isEmpty() && searchQuery.isBlank()) {
                    val allCodes = withContext(Dispatchers.IO) { localDatasource.getAllAssetCodes() }
                    statusText = if (allCodes.isEmpty()) {
                        "本地无数据，请导入资产Excel文件"
                    } else {
                        "所有资产都已盘点完毕！"
                    }
                } else {
                    statusText = "加载本地数据成功！共 ${unscannedAssetList.size} 项待盘点"
                }
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadFromLocalDb() }

    LaunchedEffect(assetBeingProcessed) {
        assetBeingProcessed?.let { assetCode ->
            withContext(Dispatchers.IO) {
                localDatasource.markAssetAsScanned(assetCode)
            }
            loadFromLocalDb()

            val timeoutResult = withTimeoutOrNull(1000L) {
                snackbarHostState.showSnackbar(
                    message = "已盘点: $assetCode",
                    actionLabel = "撤销",
                    duration = SnackbarDuration.Indefinite
                )
            }

            if (timeoutResult == SnackbarResult.ActionPerformed) {
                withContext(Dispatchers.IO) {
                    localDatasource.unmarkAssetAsScanned(assetCode)
                }
                loadFromLocalDb()
            }
            assetBeingProcessed = null
        }
    }

    val clearCache: () -> Unit = {
        coroutineScope.launch(Dispatchers.IO) {
            localDatasource.clearAllAssets()
            withContext(Dispatchers.Main) {
                searchQuery = ""
                loadFromLocalDb()
                Toast.makeText(context, "本地数据已清除", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val filteredAssetList = remember(searchQuery, unscannedAssetList) {
        if (searchQuery.isBlank()) {
            unscannedAssetList
        } else {
            unscannedAssetList.filter {
                it.assetCode.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            coroutineScope.launch {
                isLoading = true
                statusText = "正在解析 ${uris.size} 个文件..."
                val result = withContext(Dispatchers.IO) { ExcelParser.parseExcelFiles(context, uris) }
                result.onSuccess { assets ->
                    withContext(Dispatchers.IO) { localDatasource.replaceAllAssets(assets) }
                    withContext(Dispatchers.Main){
                        loadFromLocalDb()
                        Toast.makeText(context, "成功导入 ${assets.size} 条资产数据！", Toast.LENGTH_LONG).show()
                    }
                }.onFailure { error ->
                    statusText = "导入失败: ${error.message}"
                    Toast.makeText(context, "导入失败: ${error.message}", Toast.LENGTH_LONG).show()
                }
                isLoading = false
            }
        }
    }

    if (showScannerDialog) {
        Dialog(
            onDismissRequest = { showScannerDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                ScannerWithFlashlight(
                    onClose = { showScannerDialog = false },
                    onBarcodeScanned = { rawScannedContent ->
                        showScannerDialog = false
                        coroutineScope.launch {
                            isLoading = true
                            statusText = "收到数据，正在本地匹配..."
                            try {
                                val allAssetCodes = withContext(Dispatchers.IO) { localDatasource.getAllAssetCodes() }
                                val noChinese = rawScannedContent.replace(Regex("[\\u4e00-\\u9fa5]"), "")
                                val cleanedText = noChinese.replace(Regex("\\d{4}-\\d{2}-\\d{2}$"), "").trim()
                                val foundCodes = allAssetCodes.filter { code -> cleanedText.contains(code) }
                                val bestMatchCode = foundCodes.maxByOrNull { it.length }

                                if (bestMatchCode != null) {
                                    assetBeingProcessed = bestMatchCode
                                } else {
                                    statusText = "匹配失败: 本地数据库中未找到该资产"
                                    Toast.makeText(context, "匹配失败，本地无此资产", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                statusText = "处理失败: ${e.message}"
                                Log.e("LocalError", "Failed to process scan data", e)
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                )
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("BBMG-A-M") },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Routes.SCANNED_LIST) },
                        enabled = assetBeingProcessed == null
                    ) {
                        Icon(Icons.Default.FactCheck, contentDescription = "查看已盘点列表")
                    }
                    IconButton(
                        onClick = { fileExporterLauncher.launch("未盘点资产.xlsx") },
                        enabled = unscannedAssetList.isNotEmpty() && assetBeingProcessed == null
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = "导出未盘点列表")
                    }
                    IconButton(
                        onClick = { filePickerLauncher.launch("*/*") },
                        enabled = !isLoading && assetBeingProcessed == null
                    ) { Icon(Icons.Filled.FileUpload, "从文件导入资产") }
                    IconButton(
                        onClick = { dialogToShow = DialogType.CLEAR_CACHE },
                        enabled = !isLoading && assetBeingProcessed == null
                    ) { Icon(Icons.Filled.DeleteSweep, "清除所有数据") }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isLoading) "加载中..." else if(assetBeingProcessed != null) "处理中..." else statusText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = { showScannerDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isLoading && assetBeingProcessed == null
                ) {
                    Text("开始扫描", fontSize = 18.sp)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("通过资产编码搜索手动盘点") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                enabled = assetBeingProcessed == null
            )

            if (isLoading) {
                Spacer(Modifier.height(20.dp))
                CircularProgressIndicator()
            } else if (unscannedAssetList.isEmpty() && searchQuery.isBlank()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = statusText,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { navController.navigate(Routes.SCANNED_LIST) }) {
                            Icon(Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("查看已盘点列表")
                        }
                    }
                }
            } else if (searchQuery.isNotBlank() && filteredAssetList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "未找到匹配的资产",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    item {
                        Text(
                            if (searchQuery.isBlank()) "剩余 ${filteredAssetList.size} 项未盘点"
                            else "找到 ${filteredAssetList.size} 个匹配项",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(filteredAssetList, key = { it.assetCode }) { asset ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(enabled = assetBeingProcessed == null) { navController.navigate(Routes.assetDetail(asset.assetCode)) }
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 12.dp, bottom = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(asset.assetName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("编码: ${asset.assetCode}", fontSize = 14.sp)
                                    Text("位置: ${asset.location ?: "N/A"}", fontSize = 14.sp, color = Color.Gray)
                                }
                                TextButton(
                                    onClick = {
                                        assetBeingProcessed = asset.assetCode
                                    },
                                    enabled = assetBeingProcessed == null,
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text("盘点")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (dialogToShow == DialogType.CLEAR_CACHE) {
        ConfirmationDialog(
            title = "确认清除数据？",
            text = "这将删除所有已导入的资产数据，您的盘点进度也会丢失。确定要清除吗？",
            onConfirm = { clearCache() },
            onDismiss = { dialogToShow = DialogType.NONE }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailScreen(navController: NavHostController, assetCode: String, localDatasource: AssetLocalDatasource) {
    var asset by remember { mutableStateOf<Asset?>(null) }

    LaunchedEffect(assetCode) {
        asset = withContext(Dispatchers.IO) {
            localDatasource.getAssetByCode(assetCode)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("资产详情") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        asset?.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item { DetailHeader(it.assetName) }
                item { DetailRow("资产编码", it.assetCode) }
                item { DetailRow("卡片编号", it.cardNo) }
                item { DetailRow("状态", it.status, if (it.status == "已盘点") Color(0xFF4CAF50) else Color(0xFFF44336)) }
                item { DetailRow("存放地点", it.location) }
                item { DetailRow("规格", it.specification) }
                item { DetailRow("型号", it.model) }
                item { DetailRow("数量", it.quantity) }
                item { DetailRow("本币原值", it.originalValue) }
                item { DetailRow("累计折旧", it.accumulatedDepreciation) }
                item { DetailRow("本年折旧", it.currentYearDepreciation) }
                item { DetailRow("净值", it.netValue) }
                item { DetailRow("减值准备", it.impairmentProvision) }
                item { DetailRow("净额", it.netAmount) }
                item { DetailRow("资产类别", it.assetCategory) }
                item { DetailRow("管理部门", it.department) }
                item { DetailRow("使用部门", it.usingDepartment) }
                item { DetailRow("使用人", it.user) }
                item { DetailRow("开始使用日期", it.startDate) }
                item { DetailRow("使用月限", it.usageMonths) }
                item { DetailRow("已计提期初", it.initialDepreciation) }
                item { DetailRow("月折旧额", it.monthlyDepreciation) }
                item { DetailRow("净残值", it.netSalvageValue) }
                item { DetailRow("公司名称", it.companyName) }
                item { DetailRow("进项税", it.inputTax) }
                item { DetailRow("净残值率", it.netSalvageRate) }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DetailHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    )
}

@Composable
fun DetailRow(label: String, value: String?, valueColor: Color = LocalContentColor.current) {
    if (!value.isNullOrBlank()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.4f)
            )
            Text(
                text = value,
                modifier = Modifier.weight(0.6f),
                color = valueColor
            )
        }
        Divider()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BBMGZEBRATheme {
        AppNavigator()
    }
}