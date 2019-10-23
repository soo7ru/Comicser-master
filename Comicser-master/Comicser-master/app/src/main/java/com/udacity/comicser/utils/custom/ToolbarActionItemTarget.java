/*
 * Copyright 2014 Alex Curran
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/*
 * Source: https://github.com/amlcurran/ShowcaseView
 */

package com.udacity.comicser.utils.custom;

import android.graphics.Point;
import androidx.annotation.IdRes;
import androidx.appcompat.widget.Toolbar;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class ToolbarActionItemTarget implements Target {

  private final Toolbar toolbar;
  private final int menuItemId;

  public ToolbarActionItemTarget(Toolbar toolbar, @IdRes int itemId) {
    this.toolbar = toolbar;
    this.menuItemId = itemId;
  }

  @Override
  public Point getPoint() {
    return new ViewTarget(toolbar.findViewById(menuItemId)).getPoint();
  }

}
