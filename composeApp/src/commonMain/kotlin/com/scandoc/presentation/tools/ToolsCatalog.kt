package com.scandoc.presentation.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MergeType
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.Compress
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoFilter
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.RestoreFromTrash
import androidx.compose.material.icons.outlined.Screenshot
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.ViewDay
import androidx.compose.material.icons.outlined.ViewInAr
import com.scandoc.presentation.theme.ScanDocColors

object ToolsCatalog {
    val sections: List<ToolSection> = listOf(
        ToolSection(
            title = "Scanner",
            items = listOf(
                ToolItem("id_card", "ID Card", Icons.Outlined.Badge, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("extract_text", "Extract Text", Icons.Outlined.TextFields, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("id_photo", "ID Photo", Icons.Outlined.Person, ScanDocColors.ToolTintPurple, ScanDocColors.ToolIconPurple),
                ToolItem("formula", "Formula", Icons.Outlined.Functions, ScanDocColors.ToolTintPurple, ScanDocColors.ToolIconPurple),
                ToolItem("convert_photo", "Convert Photo", Icons.Outlined.PhotoFilter, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("book", "Book", Icons.AutoMirrored.Outlined.MenuBook, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("ppt", "PPT", Icons.Outlined.Slideshow, ScanDocColors.ToolTintOrange, ScanDocColors.ToolIconOrange),
                ToolItem("whiteboard", "Whiteboard", Icons.Outlined.Dashboard, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("timestamp", "Timestamp", Icons.Outlined.AccessTime, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
            ),
        ),
        ToolSection(
            title = "Import",
            items = listOf(
                ToolItem("import_images", "Import Images", Icons.Outlined.Image, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("import_files", "Import Files", Icons.Outlined.FolderOpen, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
            ),
        ),
        ToolSection(
            title = "Convert",
            items = listOf(
                ToolItem("to_word", "To Word", Icons.Outlined.Description, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("to_excel", "To Excel", Icons.Outlined.TableChart, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("to_ppt", "To PPT", Icons.Outlined.Slideshow, ScanDocColors.ToolTintOrange, ScanDocColors.ToolIconOrange),
                ToolItem("pdf_to_images", "PDF to Images", Icons.Outlined.Image, ScanDocColors.ToolTintPink, ScanDocColors.ToolIconPink),
                ToolItem("pdf_to_long", "PDF Long Image", Icons.Outlined.ViewDay, ScanDocColors.ToolTintPink, ScanDocColors.ToolIconPink),
            ),
        ),
        ToolSection(
            title = "Edit",
            items = listOf(
                ToolItem("signature", "Signature", Icons.Outlined.Draw, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("watermark", "Watermark", Icons.Outlined.Verified, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("eraser", "Smart Eraser", Icons.Outlined.AutoFixHigh, ScanDocColors.ToolTintPurple, ScanDocColors.ToolIconPurple),
                ToolItem("remove_marks", "Remove Marks", Icons.Outlined.CleaningServices, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("restore_photo", "Restore Photo", Icons.Outlined.RestoreFromTrash, ScanDocColors.ToolTintOrange, ScanDocColors.ToolIconOrange),
                ToolItem("merge", "Merge Files", Icons.AutoMirrored.Outlined.MergeType, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("extract_pages", "Extract Pages", Icons.Outlined.ContentCut, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("reorder_pages", "Reorder Pages", Icons.Outlined.SwapVert, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("lock", "Lock", Icons.Outlined.Lock, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("compress", "Compress", Icons.Outlined.Compress, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
            ),
        ),
        ToolSection(
            title = "Utilities",
            items = listOf(
                ToolItem("measure", "Measure", Icons.Outlined.Straighten, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("solver_ai", "Solver AI", Icons.Outlined.AutoAwesome, ScanDocColors.ToolTintPurple, ScanDocColors.ToolIconPurple),
                ToolItem("count_cam", "CountCam", Icons.Outlined.Tag, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("scroll_capture", "Scroll Capture", Icons.Outlined.Screenshot, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("print", "Print", Icons.Outlined.Print, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("scan_3d", "3D Scan", Icons.Outlined.ViewInAr, ScanDocColors.ToolTintPurple, ScanDocColors.ToolIconPurple),
                ToolItem("qr_code", "QR Code", Icons.Outlined.QrCode, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
            ),
        ),
    )
}
