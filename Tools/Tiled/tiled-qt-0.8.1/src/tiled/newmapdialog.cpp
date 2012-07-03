/*
 * newmapdialog.cpp
 * Copyright 2009-2010, Thorbjørn Lindeijer <thorbjorn@lindeijer.nl>
 *
 * This file is part of Tiled.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

#include "newmapdialog.h"
#include "ui_newmapdialog.h"

#include "isometricrenderer.h"
#include "map.h"
#include "mapdocument.h"
#include "orthogonalrenderer.h"
#include "preferences.h"
#include "tilelayer.h"

#include <QSettings>

static const char * const ORIENTATION_KEY = "Map/Orientation";
static const char * const MAP_WIDTH_KEY = "Map/Width";
static const char * const MAP_HEIGHT_KEY = "Map/Height";
static const char * const TILE_WIDTH_KEY = "Map/TileWidth";
static const char * const TILE_HEIGHT_KEY = "Map/TileHeight";

using namespace Tiled;
using namespace Tiled::Internal;

NewMapDialog::NewMapDialog(QWidget *parent) :
    QDialog(parent),
    mUi(new Ui::NewMapDialog)
{
    mUi->setupUi(this);
    setWindowFlags(windowFlags() & ~Qt::WindowContextHelpButtonHint);

    // Restore previously used settings
    QSettings *s = Preferences::instance()->settings();
    const int orientation = s->value(QLatin1String(ORIENTATION_KEY)).toInt();
    const int mapWidth = s->value(QLatin1String(MAP_WIDTH_KEY), 100).toInt();
    const int mapHeight = s->value(QLatin1String(MAP_HEIGHT_KEY), 100).toInt();
    const int tileWidth = s->value(QLatin1String(TILE_WIDTH_KEY), 32).toInt();
    const int tileHeight = s->value(QLatin1String(TILE_HEIGHT_KEY),
                                    32).toInt();

    mUi->orientation->setCurrentIndex(orientation);
    mUi->mapWidth->setValue(mapWidth);
    mUi->mapHeight->setValue(mapHeight);
    mUi->tileWidth->setValue(tileWidth);
    mUi->tileHeight->setValue(tileHeight);

    // Make the font of the pixel size label smaller
    QFont font = mUi->pixelSizeLabel->font();
    const qreal size = QFontInfo(font).pointSizeF();
    font.setPointSizeF(size - 1);
    mUi->pixelSizeLabel->setFont(font);

    connect(mUi->mapWidth, SIGNAL(valueChanged(int)), SLOT(refreshPixelSize()));
    connect(mUi->mapHeight, SIGNAL(valueChanged(int)), SLOT(refreshPixelSize()));
    connect(mUi->tileWidth, SIGNAL(valueChanged(int)), SLOT(refreshPixelSize()));
    connect(mUi->tileHeight, SIGNAL(valueChanged(int)), SLOT(refreshPixelSize()));
    connect(mUi->orientation, SIGNAL(currentIndexChanged(int)), SLOT(refreshPixelSize()));
    refreshPixelSize();
}

NewMapDialog::~NewMapDialog()
{
    delete mUi;
}

MapDocument *NewMapDialog::createMap()
{
    if (exec() != QDialog::Accepted)
        return 0;

    const int mapWidth = mUi->mapWidth->value();
    const int mapHeight = mUi->mapHeight->value();
    const int tileWidth = mUi->tileWidth->value();
    const int tileHeight = mUi->tileHeight->value();
    const int orientation = mUi->orientation->currentIndex();

    Map *map = new Map((orientation == 0) ? Map::Orthogonal : Map::Isometric,
                       mapWidth, mapHeight, tileWidth, tileHeight);

    // Add one filling tile layer to new maps
    map->addLayer(new TileLayer(tr("Tile Layer 1"), 0, 0, mapWidth, mapHeight));

    // Store settings for next time
    QSettings *s = Preferences::instance()->settings();
    s->setValue(QLatin1String(ORIENTATION_KEY), orientation);
    s->setValue(QLatin1String(MAP_WIDTH_KEY), mapWidth);
    s->setValue(QLatin1String(MAP_HEIGHT_KEY), mapHeight);
    s->setValue(QLatin1String(TILE_WIDTH_KEY), tileWidth);
    s->setValue(QLatin1String(TILE_HEIGHT_KEY), tileHeight);

    return new MapDocument(map);
}

void NewMapDialog::refreshPixelSize()
{
    const int orientation = mUi->orientation->currentIndex();
    const Map map((orientation == 0) ? Map::Orthogonal : Map::Isometric,
                  mUi->mapWidth->value(),
                  mUi->mapHeight->value(),
                  mUi->tileWidth->value(),
                  mUi->tileHeight->value());

    QSize size;

    switch (map.orientation()) {
    case Map::Isometric:
        size = IsometricRenderer(&map).mapSize();
        break;
    default:
        size = OrthogonalRenderer(&map).mapSize();
        break;
    }

    mUi->pixelSizeLabel->setText(tr("%1 x %2 pixels")
                                 .arg(size.width())
                                 .arg(size.height()));
}
