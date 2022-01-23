package com.nevmem.survey.fs

import com.nevmem.survey.fs.internal.FileSystemServiceImpl

fun createFileSystemService(): FileSystemService = FileSystemServiceImpl()
