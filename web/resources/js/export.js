var faker = window.faker;
var base64Img = null;
var servoHeaderImg;
var servoImg;
var createPDF = {};

function loadImages() {
    servoHeaderImg = new Image();
    servoImg = new Image();
    servoHeaderImg.src = '/ServoCMP/resources/images/servoHeader.jpg';
    servoImg.src = '/ServoCMP/resources/images/servo.jpg';
}

// Default - shows what a default table looks like
createPDF.auto = function () {
    var doc = new jsPDF();
    doc.autoTable(getColumns(), getData());
    return doc;
};

// Minimal - shows how compact tables can be drawn
createPDF.minimal = function () {
    var doc = new jsPDF();
    doc.autoTable(getColumns(), getData(), {
        tableWidth: 'wrap',
        styles: {cellPadding: 0.5, fontSize: 8}
    });
    return doc;
};

// Long data - shows how the overflow features looks and can be used
createPDF.long = function () {
    var doc = new jsPDF('l');
    var columnsLong = getColumns().concat([
        {title: "Title with\nlinebreak", dataKey: "text2"},
        {title: "Long text column", dataKey: "text"},
    ]);

    doc.text(7, 15, "Overflow 'ellipsize' (default)");
    doc.autoTable(columnsLong, getData(), {
        startY: 20,
        margin: {horizontal: 7},
        styles: {columnWidth: 'wrap'},
        columnStyles: {text: {columnWidth: 'auto'}}
    });

    doc.text("Overflow 'hidden'", 7, doc.autoTable.previous.finalY + 10);
    doc.autoTable(columnsLong, getData(), {
        startY: doc.autoTable.previous.finalY + 15,
        margin: {horizontal: 7},
        styles: {overflow: 'hidden', columnWidth: 'wrap'},
        columnStyles: {text: {columnWidth: 'auto'}}
    });

    doc.text("Overflow 'linebreak'", 7, doc.autoTable.previous.finalY + 10);
    doc.autoTable(columnsLong, getData(3), {
        startY: doc.autoTable.previous.finalY + 15,
        margin: {horizontal: 7},
        bodyStyles: {valign: 'top'},
        styles: {overflow: 'linebreak', columnWidth: 'wrap'},
        columnStyles: {text: {columnWidth: 'auto'}}
    });

    return doc;
};

// Content - shows how tables can be integrated with any other pdf content
createPDF.content = function () {
    var doc = new jsPDF();

    doc.setFontSize(18);
    doc.text('A story about a person', 14, 22);
    doc.setFontSize(11);
    doc.setTextColor(100);
    var text = doc.splitTextToSize(shuffleSentence(faker.lorem.words(55)) + '.', doc.internal.pageSize.width - 35, {});
    doc.text(text, 14, 30);

    var cols = getColumns();
    cols.splice(0, 2);
    doc.autoTable(cols, getData(40), {startY: 50, showHeader: 'firstPage'});

    doc.text(text, 14, doc.autoTable.previous.finalY + 10);

    return doc;
};

// Multiple - shows how multiple tables can be drawn both horizontally and vertically
createPDF.multiple = function () {
    var doc = new jsPDF();
    doc.setFontSize(22);
    doc.text("Multiple tables", 14, 20);
    doc.setFontSize(12);

    doc.autoTable(getColumns(), getData(25), {startY: 30});

    doc.autoTable(getColumns().slice(0, 2), getData(15), {
        startY: 240,
        showHeader: 'firstPage',
        margin: {right: 107}
    });

    doc.setPage(1 + doc.internal.getCurrentPageInfo().pageNumber - doc.autoTable.previous.pageCount);

    doc.autoTable(getColumns().slice(0, 2), getData(15), {
        startY: 240,
        showHeader: 'firstPage',
        margin: {left: 107}
    });

    for (var j = 0; j < 6; j++) {
        doc.autoTable(getColumns(), getData(9), {
            startY: doc.autoTable.previous.finalY + 10,
            pageBreak: 'avoid'
        });
    }

    return doc;
};

createPDF.html = function (sampleID, remarks, single) {
    var imgHeight = 75;
    var positionY = 10;
    var doc = new jsPDF();
    var pageHeight = doc.internal.pageSize.height;
    var custTable = doc.autoTableHtmlToJson(document.getElementById("custTableId"));
//    var iocTable = doc.autoTableHtmlToJson(document.getElementById("iocTableId"));
    var iocTable1 = doc.autoTableHtmlToJson($(".iocTable")[0]);
    var iocTable2;
    if ($(".iocTable")[1]) {
        iocTable2 = doc.autoTableHtmlToJson($(".iocTable")[1]);
    }
    var deptTable = doc.autoTableHtmlToJson(document.getElementById("deptTableId"));
    var MarketTest = document.getElementById("Market-Test");
    var marketTest = doc.autoTableHtmlToJson(MarketTest);
    doc.addImage(servoHeaderImg, 'JPEG', 14, positionY, 185.208333, 26.19375);
    doc.autoTable(custTable.columns, custTable.data, {
        startY: 40,
        createdHeaderCell: function (cell, data) {
            if (data.column.index === 0 || data.column.index === 2) {
                cell.styles.fontStyle = 'bold';
            }
            if (data.column.index === 1 || data.column.index === 3) {
                cell.styles.textColor = 0;
            }
        },
        createdCell: function (cell, data) {
            if (data.column.index === 0 || data.column.index === 2) {
                cell.styles.fontStyle = 'bold';
            }
            if (data.column.index === 1 || data.column.index === 3) {
                cell.styles.textColor = 0;
            }
        },
        table: {fillColor: 255, fontStyle: 'normal', lineWidth: 0.5, textColor: [192, 80, 77], lineColor: [192, 80, 77]},
        theme: 'plain, [192, 80, 77]',
        alternateRow: {fillColor: [239, 211, 210]}
    });
    doc.autoTable(iocTable1.columns, iocTable1.data, {
        startY: doc.autoTableEndPosY() + 10,
        theme: 'plain, [54, 95, 145]',
        createdHeaderCell: function (cell, data) {
            if (data.column.index === 0 || data.column.index === 2) {
                cell.styles.fontStyle = 'bold';
            }
            if (data.column.index === 1 || data.column.index === 3) {
                cell.styles.textColor = 0;
            }
        },
        createdCell: function (cell, data) {
            if (data.column.index === 0 || data.column.index === 2) {
                cell.styles.fontStyle = 'bold';
            }
            if (data.column.index === 1 || data.column.index === 3) {
                cell.styles.textColor = 0;
            }
        },
        textColor: [79, 129, 189],
        lineColor: [79, 129, 189],
        alternateRow: {fillColor: [211, 223, 238]}
    });
    if ($(".iocTable")[1]) {
        doc.autoTable(iocTable2.columns, iocTable2.data, {
            startY: doc.autoTableEndPosY() + 10,
            theme: 'plain, [54, 95, 145]',
            createdHeaderCell: function (cell, data) {
                if (data.column.index === 0 || data.column.index === 2) {
                    cell.styles.fontStyle = 'bold';
                }
                if (data.column.index === 1 || data.column.index === 3) {
                    cell.styles.textColor = 0;
                }
            },
            createdCell: function (cell, data) {
                if (data.column.index === 0 || data.column.index === 2) {
                    cell.styles.fontStyle = 'bold';
                }
                if (data.column.index === 1 || data.column.index === 3) {
                    cell.styles.textColor = 0;
                }
            },
            textColor: [79, 129, 189],
            lineColor: [79, 129, 189],
            alternateRow: {fillColor: [211, 223, 238]}
        });
    }
    doc.autoTable(deptTable.columns, deptTable.data, {
        startY: doc.autoTableEndPosY() + 10,
        theme: 'plain, 0',
        createdHeaderCell: function (cell, data) {
            if (data.column.index === 0 || data.column.index === 2) {
                cell.styles.fontStyle = 'bold';
            }
            if (data.column.index === 1 || data.column.index === 3) {
                cell.styles.textColor = 0;
            }
        },
        createdCell: function (cell, data) {
            if (data.column.index === 0 || data.column.index === 2) {
                cell.styles.fontStyle = 'bold';
            }
            if (data.column.index === 1 || data.column.index === 3) {
                cell.styles.textColor = 0;
            }
        },
        table: {lineColor: [155, 187, 89]},
        alternateRow: {fillColor: [230, 238, 213]}
    });
    doc.autoTable(marketTest.columns, marketTest.data, {
        startY: doc.autoTableEndPosY() + 10,
        theme: 'plain, [60, 70, 143]',
        createdHeaderCell: function (cell, data) {
            cell.styles.fillColor = [169, 173, 218];
            cell.styles.fontStyle = 'bold';
        },
        createdCell: function (cell, data) {
            if (data.column.index === 0 || data.column.index === 1 || data.column.index === 2 || data.column.index === 6) {
                cell.styles.fontStyle = 'bold';
            }
        },
        table: {lineColor: [60, 70, 143]},
        alternateRow: {fillColor: [226, 228, 243]}
    });

    // doc.text("Remarks: " + remarks, 14, doc.autoTableEndPosY() + 10);
    var vRemarks = "        " + remarks;
    var lMargin = 14; //left margin in mm
    var rMargin = 14; //right margin in mm
    var pdfInMM = 310.208333;  // width of A4 in mm
    var lines = doc.splitTextToSize(vRemarks, (pdfInMM - lMargin - rMargin - 4));
    doc.setFontSize(12);
    doc.setFontType("bold");
    doc.text("Remarks: ", 14, doc.autoTableEndPosY() + 10);
    doc.setFontSize(11);
    doc.setFontType("normal");
    doc.text(14, doc.autoTableEndPosY() + 15, lines);

    if (single != true) {
        doc.addPage();
        var add = imgHeight + positionY;
        doc.addImage(servoImg, 'JPEG', 80, positionY, 51.59375, 19.84375);
        positionY += 24.84375;
        doc.addImage(servoImg, 'JPEG', 40, positionY, 123, 82);
        $('.charts').each(function (index) {
            var add = Number(imgHeight) + Number(positionY);
            if (positionY > pageHeight - 16 || add > pageHeight) {
                doc.addPage();
                positionY = 44;
            }
            var imageData = $(this).highcharts().createCanvas();
            doc.addImage(imageData, 'JPEG', 40, positionY, 122.502083, 81.75625);
            positionY += 86;
        });
    }
    doc.setFontSize(11);
    return doc;
};
createPDF['header-footer'] = function () {
    var doc = new jsPDF();
    var totalPagesExp = "{total_pages_count_string}";
    var pageContent = function (data) {
        // HEADER
        doc.setFontSize(20);
        doc.setTextColor(40);
        doc.setFontStyle('normal');
        if (base64Img) {
            doc.addImage(base64Img, 'JPEG', data.settings.margin.left, 15, 10, 10);
        }
        doc.text("Report", data.settings.margin.left + 15, 22);
        // FOOTER
        var str = "Page " + data.pageCount;
        // Total page number plugin only available in jspdf v1.0+
        if (typeof doc.putTotalPages === 'function') {
            str = str + " of " + totalPagesExp;
        }
        doc.setFontSize(10);
        doc.text(str, data.settings.margin.left, doc.internal.pageSize.height - 10);
    };
    doc.autoTable(getColumns(), getData(40), {
        addPageContent: pageContent,
        margin: {top: 30}
    });
    // Total page number plugin only available in jspdf v1.0+
    if (typeof doc.putTotalPages === 'function') {
        doc.putTotalPages(totalPagesExp);
    }

    return doc;
};
// Minimal - shows how compact tables can be drawn
createPDF.defaults = function () {
    // Global defaults
    jsPDF.autoTableSetDefaults({
        columnStyles: {id: {fontStyle: 'bold'}},
        headerStyles: {fillColor: 0},
    });
    var doc = new jsPDF();
    // Document defaults
    doc.autoTableSetDefaults({
        headerStyles: {fillColor: [155, 89, 182]}, // Purple
        margin: {top: 25},
        addPageContent: function (data) {
            doc.setFontSize(20);
            doc.text('Document specific header', data.settings.margin.left, 20);
        }
    });
    doc.autoTable(getColumns(), getData());
    doc.addPage();
    doc.autoTable(getColumns(), getData(), {
        // Will override document and global headerStyles
        headerStyles: {fillColor: [231, 76, 60]} // Red
    });
    // Reset defaults
    doc.autoTableSetDefaults(null);
    jsPDF.autoTableSetDefaults(null);
    return doc;
};
// Horizontal - shows how tables can be drawn with horizontal headers
createPDF.horizontal = function () {
    var doc = new jsPDF();
    doc.autoTable(getColumns().splice(1, 4), getData(), {
        showHeader: 'never',
        columnStyles: {
            name: {fillColor: [41, 128, 185], textColor: 255, fontStyle: 'bold'}
        }
    });
    return doc;
};
// Custom style - shows how custom styles can be applied to tables
createPDF.spans = function () {
    var doc = new jsPDF('p', 'pt');
    doc.setFontSize(12);
    doc.setTextColor(0);
    doc.setFontStyle('bold');
    doc.text('Col and row span', 40, 50);
    var data = getData(80);
    data.sort(function (a, b) {
        return parseFloat(b.expenses) - parseFloat(a.expenses);
    });
    doc.autoTable(getColumns(), data, {
        theme: 'grid',
        startY: 60,
        drawRow: function (row, data) {
            // Colspan
            doc.setFontStyle('bold');
            doc.setFontSize(10);
            if (row.index === 0) {
                doc.setTextColor(200, 0, 0);
                doc.rect(data.settings.margin.left, row.y, data.table.width, 20, 'S');
                doc.autoTableText("Priority Group", data.settings.margin.left + data.table.width / 2, row.y + row.height / 2, {
                    halign: 'center',
                    valign: 'middle'
                });
                data.cursor.y += 20;
            } else if (row.index === 5) {
                doc.rect(data.settings.margin.left, row.y, data.table.width, 20, 'S');
                doc.autoTableText("Other Groups", data.settings.margin.left + data.table.width / 2, row.y + row.height / 2, {
                    halign: 'center',
                    valign: 'middle'
                });
                data.cursor.y += 20;
            }

            if (row.index % 5 === 0) {
                var posY = row.y + row.height * 6 + data.settings.margin.bottom;
                if (posY > doc.internal.pageSize.height) {
                    data.addPage();
                }
            }
        },
        drawCell: function (cell, data) {
            // Rowspan
            if (data.column.dataKey === 'id') {
                if (data.row.index % 5 === 0) {
                    doc.rect(cell.x, cell.y, data.table.width, cell.height * 5, 'S');
                    doc.autoTableText(data.row.index / 5 + 1 + '', cell.x + cell.width / 2, cell.y + cell.height * 5 / 2, {
                        halign: 'center',
                        valign: 'middle'
                    });
                }
                return false;
            }
        }
    });
    return doc;
};
// Themes - shows how the different themes looks
createPDF.themes = function () {
    var doc = new jsPDF();
    doc.setFontSize(12);
    doc.setFontStyle('bold');
    doc.text('Theme "striped"', 14, 16);
    doc.autoTable(getColumns(), getData(), {startY: 20});
    doc.text('Theme "grid"', 14, doc.autoTable.previous.finalY + 10);
    doc.autoTable(getColumns(), getData(), {startY: doc.autoTable.previous.finalY + 14, theme: 'grid'});
    doc.text('Theme "plain"', 14, doc.autoTable.previous.finalY + 10);
    doc.autoTable(getColumns(), getData(), {startY: doc.autoTable.previous.finalY + 14, theme: 'plain'});
    return doc;
};
// Custom style - shows how custom styles can be applied to tables
createPDF.custom = function () {
    var doc = new jsPDF();
    doc.autoTable(getColumns().slice(1, 5), getData(20), {
        tableLineColor: [189, 195, 199],
        tableLineWidth: 0.75,
        styles: {
            font: 'courier',
            lineColor: [44, 62, 80],
            lineWidth: 0.75
        },
        headerStyles: {
            fillColor: [44, 62, 80],
            fontSize: 15
        },
        bodyStyles: {
            fillColor: [52, 73, 94],
            textColor: 240
        },
        alternateRowStyles: {
            fillColor: [74, 96, 117]
        },
        columnStyles: {
            email: {
                fontStyle: 'bold'
            }
        },
        /*parsedInput: function (cell, data) {
         if (data.column.dataKey === 'expenses') {
         cell.styles.halign = 'right';
         if (cell.raw > 600) {
         cell.styles.textColor = [255, 100, 100];
         cell.styles.fontStyle = 'bolditalic';
         }
         cell.text = '$' + cell.text;
         } else if (data.column.dataKey === 'name') {
         cell.text = cell.raw.split(' ')[0]; // only first name
         }
         }*/
    });
    return doc;
};
/*
 |--------------------------------------------------------------------------
 | Below is some helper functions for the createPDF
 |--------------------------------------------------------------------------
 */



// Returns a new array each time to avoid pointer issues
var getColumns = function () {
    return [
        {title: "ID", dataKey: "id"},
        {title: "Name", dataKey: "name"},
        {title: "Email", dataKey: "email"},
        {title: "City", dataKey: "city"},
        {title: "Expenses", dataKey: "expenses"}
    ];
};
// Uses the faker.js library to get random data.
function getData(rowCount) {
    rowCount = rowCount || 4;
    //var sentence = "Minima quis totam nobis nihil et molestiae architecto accusantium qui necessitatibus sit ducimus cupiditate qui ullam et aspernatur esse et dolores ut voluptatem odit quasi ea sit ad sint voluptatem est dignissimos voluptatem vel adipisci facere consequuntur et reprehenderit cum unde debitis ab cumque sint quo ut officiis rerum aut quia quia expedita ut consectetur animiqui voluptas suscipit Monsequatur";
    var sentence = faker.lorem.words(20);
    var data = [];
    for (var j = 1; j <= rowCount; j++) {
        data.push({
            id: j,
            name: faker.name.findName(),
            email: faker.internet.email(),
            country: faker.address.country(),
            city: faker.address.city(),
            expenses: faker.finance.amount(),
            text: shuffleSentence(sentence),
            text2: faker.lorem.words(1)
        });
    }
    return data;
}

function shuffleSentence(words) {
    if (typeof words === 'string')
        return words;
    words = words || faker.lorem.words(8);
    var str = faker.helpers.shuffle(words).join(' ').trim();
    return str.charAt(0).toUpperCase() + str.slice(1);
}

imgToBase64('document.jpg', function (base64) {
    base64Img = base64;
});
// You could either use a function similar to this or pre convert an image with for example http://dopiaza.org/tools/datauri
// http://stackoverflow.com/questions/6150289/how-to-convert-image-into-base64-string-using-javascript
function imgToBase64(url, callback) {
    if (!window.FileReader) {
        callback(null);
        return;
    }
    var xhr = new XMLHttpRequest();
    xhr.responseType = 'blob';
    xhr.onload = function () {
        var reader = new FileReader();
        reader.onloadend = function () {
            callback(reader.result.replace('text/xml', 'image/jpeg'));
        };
        reader.readAsDataURL(xhr.response);
    };
    xhr.open('GET', url);
    xhr.send();
}