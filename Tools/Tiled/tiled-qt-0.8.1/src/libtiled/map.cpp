/*
 * map.cpp
 * Copyright 2008-2010, Thorbjørn Lindeijer <thorbjorn@lindeijer.nl>
 * Copyright 2008, Roderic Morris <roderic@ccs.neu.edu>
 * Copyright 2010, Andrew G. Crowell <overkill9999@gmail.com>
 *
 * This file is part of libtiled.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#include "map.h"

#include "layer.h"
#include "tile.h"
#include "tilelayer.h"
#include "tileset.h"

using namespace Tiled;

Map::Map(Orientation orientation,
         int width, int height, int tileWidth, int tileHeight):
    mOrientation(orientation),
    mWidth(width),
    mHeight(height),
    mTileWidth(tileWidth),
    mTileHeight(tileHeight)
{
}

Map::~Map()
{
    qDeleteAll(mLayers);
}

static QMargins maxMargins(const QMargins &a,
                           const QMargins &b)
{
    return QMargins(qMax(a.left(), b.left()),
                    qMax(a.top(), b.top()),
                    qMax(a.right(), b.right()),
                    qMax(a.bottom(), b.bottom()));
}

void Map::adjustDrawMargins(const QMargins &margins)
{
    // The TileLayer includes the maximum tile size in its draw margins. So
    // we need to subtract the tile size of the map, since that part does not
    // contribute to additional margin.
    mDrawMargins = maxMargins(QMargins(margins.left(),
                                       margins.top() - mTileHeight,
                                       margins.right() - mTileWidth,
                                       margins.bottom()),
                              mDrawMargins);
}

int Map::tileLayerCount() const
{
    int count = 0;
    foreach (Layer *layer, mLayers)
       if (layer->asTileLayer())
           count++;
    return count;
}

int Map::objectGroupCount() const
{
    int count = 0;
    foreach (Layer *layer, mLayers)
        if (layer->asObjectGroup())
           count++;
    return count;
}

void Map::addLayer(Layer *layer)
{
    adoptLayer(layer);
    mLayers.append(layer);
}

int Map::indexOfLayer(const QString &layerName) const
{
    for (int index = 0; index < mLayers.size(); index++)
        if (layerAt(index)->name() == layerName)
            return index;

    return -1;
}

void Map::insertLayer(int index, Layer *layer)
{
    adoptLayer(layer);
    mLayers.insert(index, layer);
}

void Map::adoptLayer(Layer *layer)
{
    layer->setMap(this);

    if (TileLayer *tileLayer = dynamic_cast<TileLayer*>(layer))
        adjustDrawMargins(tileLayer->drawMargins());
}

Layer *Map::takeLayerAt(int index)
{
    Layer *layer = mLayers.takeAt(index);
    layer->setMap(0);
    return layer;
}

void Map::addTileset(Tileset *tileset)
{
    mTilesets.append(tileset);
}

void Map::insertTileset(int index, Tileset *tileset)
{
    mTilesets.insert(index, tileset);
}

int Map::indexOfTileset(Tileset *tileset) const
{
    return mTilesets.indexOf(tileset);
}

void Map::removeTilesetAt(int index)
{
    mTilesets.removeAt(index);
}

void Map::replaceTileset(Tileset *oldTileset, Tileset *newTileset)
{
    const int index = mTilesets.indexOf(oldTileset);
    Q_ASSERT(index != -1);

    foreach (Layer *layer, mLayers)
        layer->replaceReferencesToTileset(oldTileset, newTileset);

    mTilesets.replace(index, newTileset);
}

bool Map::isTilesetUsed(Tileset *tileset) const
{
    foreach (const Layer *layer, mLayers)
        if (layer->referencesTileset(tileset))
            return true;

    return false;
}

Map *Map::clone() const
{
    Map *o = new Map(mOrientation, mWidth, mHeight, mTileWidth, mTileHeight);
    o->mDrawMargins = mDrawMargins;
    foreach (const Layer *layer, mLayers)
        o->addLayer(layer->clone());
    o->mTilesets = mTilesets;
    o->setProperties(properties());
    return o;
}


QString Tiled::orientationToString(Map::Orientation orientation)
{
    switch (orientation) {
    default:
    case Map::Unknown:
        return QLatin1String("unknown");
        break;
    case Map::Orthogonal:
        return QLatin1String("orthogonal");
        break;
    case Map::Isometric:
        return QLatin1String("isometric");
        break;
    }
}

Map::Orientation Tiled::orientationFromString(const QString &string)
{
    Map::Orientation orientation = Map::Unknown;
    if (string == QLatin1String("orthogonal")) {
        orientation = Map::Orthogonal;
    } else if (string == QLatin1String("isometric")) {
        orientation = Map::Isometric;
    }
    return orientation;
}
