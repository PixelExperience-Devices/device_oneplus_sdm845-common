/*
* Copyright (C) 2018 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/

package vendor.oneplus.camera.CameraHIDL.V1_0;

import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.RemoteException;

public class IOnePlusCameraProvider {

    private static final String DESCRIPTOR =
            "vendor.oneplus.camera.CameraHIDL@1.0::IOnePlusCameraProvider";
    private static final String PACKAGE_NAME = "com.oneplus.camera"; 
    private static final int TRANSACTION_setPackageName = 2;

    private static IHwBinder sIOnePlusCameraProvider;

    public IOnePlusCameraProvider() throws RemoteException {
        sIOnePlusCameraProvider = HwBinder.getService(DESCRIPTOR, "default");
    }

    public boolean setPackageName() {
        if (sIOnePlusCameraProvider == null) {
            return false;
        }

        HwParcel data = new HwParcel();
        HwParcel reply = new HwParcel();

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            data.writeString(PACKAGE_NAME);

            sIOnePlusCameraProvider.transact(TRANSACTION_setPackageName, data, reply, 0);

            reply.verifySuccess();
            data.releaseTemporaryStorage();

            return reply.readBool();
        } catch (Throwable t) {
            return false;
        } finally {
            reply.release();
        }
    }
}

