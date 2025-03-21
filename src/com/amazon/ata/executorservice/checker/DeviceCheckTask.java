package com.amazon.ata.executorservice.checker;

import com.amazon.ata.executorservice.coralgenerated.devicecommunication.GetDeviceSystemInfoRequest;
import com.amazon.ata.executorservice.coralgenerated.devicecommunication.GetDeviceSystemInfoResponse;
import com.amazon.ata.executorservice.coralgenerated.devicecommunication.RingDeviceFirmwareVersion;
import com.amazon.ata.executorservice.devicecommunication.RingDeviceCommunicatorService;
import com.amazon.ata.executorservice.util.KnownRingDeviceFirmwareVersions;

/**
 * A task to check a single device's version against a desired latest
 * version, requesting a firmware update if appropriate.
 *
 * PARTICIPANTS: Implement this class in Phase 1
 */
public class DeviceCheckTask implements Runnable  {
    private RingDeviceCommunicatorService ringDeviceCommunicatorService;
    private DeviceChecker deviceChecker;
    private String deviceId;
    private RingDeviceFirmwareVersion targetVersion; // Set this to the desired latest firmware version.

    /**
     * Constructs a DeviceCheckTask with the given dependencies and parameters.
     *
     * PARTICIPANTS: If you add constructor parameters, add them AFTER the DeviceChecker
     * argument. If you add parameters before the DeviceChecker, your tests will fail.
     *
     * @param deviceChecker The DeviceChecker to use while executing this task
     */
    public DeviceCheckTask(DeviceChecker deviceChecker, String deviceId, RingDeviceFirmwareVersion targetVersion) {
        this.ringDeviceCommunicatorService = deviceChecker.getRingDeviceCommunicatorService();
        this.deviceChecker = deviceChecker;
        this.deviceId = deviceId;
        this.targetVersion = targetVersion;
    }

    @Override
    public void run() {//get device firmware version
        GetDeviceSystemInfoRequest request = GetDeviceSystemInfoRequest.builder().withDeviceId(deviceId).build();
        GetDeviceSystemInfoResponse response = this.ringDeviceCommunicatorService.getDeviceSystemInfo(request);
        RingDeviceFirmwareVersion firmwareVersion = response.getSystemInfo().getDeviceFirmwareVersion(); // Get the device's firmware version'
        // check if the firmware version is outdated
        if (KnownRingDeviceFirmwareVersions.needsUpdate(firmwareVersion, targetVersion)) {
            //update the device's firmware'
            this.deviceChecker.updateDevice(this.deviceId, this.targetVersion); // Update the firmware (implementation details depend on the
            // communication service used and the device's capabilities. For example, you might use the device's communication service to send a firmware update request to the device)
        }//Con este metodo void run lo que hago es obtener la version del firmware del dispositivo, compararla con la version deseada
        //si la version del firmware es antigua, se invoca al metodo updateFirmware para actualizarlo.
        // Compare the device's firmware version with the desired latest version
        // '
        //Update the device's firmware if necessary

        //update firmware if necessary
    }
}
