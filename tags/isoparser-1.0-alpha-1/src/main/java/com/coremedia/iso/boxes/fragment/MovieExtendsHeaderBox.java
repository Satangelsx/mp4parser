/*
 * Copyright 2009 castLabs GmbH, Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coremedia.iso.boxes.fragment;

import com.coremedia.iso.BoxFactory;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoInputStream;
import com.coremedia.iso.IsoOutputStream;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.FullBox;

import java.io.IOException;

/**
 * aligned(8) class MovieExtendsHeaderBox extends FullBox(�mehd�, version, 0) {
 * if (version==1) {
 * unsigned int(64) fragment_duration;
 * } else { // version==0
 * unsigned int(32) fragment_duration;
 * }
 * }
 */
public class MovieExtendsHeaderBox extends FullBox {
  public static final String TYPE = "mehd";
  private long fragmentDuration;

  public MovieExtendsHeaderBox() {
    super(IsoFile.fourCCtoBytes(TYPE));
  }

  public String getDisplayName() {
    return "Movie Extends Header Box";
  }

  @Override
  protected long getContentSize() {
    return getVersion() == 1 ? 8 : 4;
  }

  protected void getContent(IsoOutputStream os) throws IOException {
    if (getVersion() == 1) {
      os.writeUInt64(fragmentDuration);
    } else {
      os.writeUInt32(fragmentDuration);
    }
  }

  @Override
  public void parse(IsoInputStream in, long size, BoxFactory boxFactory, Box lastMovieFragmentBox) throws IOException {
    super.parse(in, size, boxFactory, lastMovieFragmentBox);

    fragmentDuration = getVersion() == 1 ? in.readUInt64() : in.readUInt32();
  }

  public long getFragmentDuration() {
    return fragmentDuration;
  }
}
